package de.hitohitonika.tcgs.cardcollectionservice.magic.imports;

import de.hitohitonika.tcgs.cardcollectionservice.data.GameType;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.ImportMetadata;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.services.MetadataRepository;
import de.hitohitonika.tcgs.cardcollectionservice.importers.DataImporter;
import de.hitohitonika.tcgs.cardcollectionservice.magic.data.entities.MagicCard;
import de.hitohitonika.tcgs.cardcollectionservice.magic.data.entities.MagicSet;
import de.hitohitonika.tcgs.cardcollectionservice.magic.data.services.MagicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.MappingIterator;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

@Slf4j
public class MagicImporter implements DataImporter {

    private final RestClient restClient;
    private RestClient downloadClient;
    private final MetadataRepository metadataRepository;
    private final ObjectMapper objectMapper;
    private final MagicService magicService;

    private int totalProcessedCards;

    MagicImporter(
            @Value("${app.imports.magic.address}") String address, MetadataRepository metadataRepository,
            MagicService magicService
    ) {
        this.restClient = RestClient.builder()
                .baseUrl(address)
                .defaultHeader("User-Agent", "Mozilla/5.0")
                .build();

        this.metadataRepository = metadataRepository;
        this.objectMapper = new ObjectMapper();
        this.magicService = magicService;
    }

    @Override
    public boolean didImportRun(){
        return false;
    }


    @Override
    public void importData() {
        log.info("Starting Magic Bulk Data check...");

        var response = restClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(MagicBulkResponse.class);

        if  (response == null || response.data() == null) {
            log.warn("Scryfall API returned empty response.");
            return;
        }

        MagicBulkEndpointDefinition allCardsMetadata = response.data().stream()
                .filter(d -> "all_cards".equals(d.type()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No 'all_cards' metadata found in Scryfall response"));

        var metadata = metadataRepository.findById(GameType.MAGIC.getKey())
                .orElse(new ImportMetadata(GameType.MAGIC.getKey(), OffsetDateTime.MIN));

        if(allCardsMetadata.updated_at().isAfter(metadata.getLastApiUpdate())) {
            log.info("New data available (API: {}, Local: {}). Starting download...",
                    allCardsMetadata.updated_at(), metadata.getLastApiUpdate());

            totalProcessedCards = 0;
            startStreamingImport(allCardsMetadata.download_uri());

            metadata.setLastApiUpdate(allCardsMetadata.updated_at());
            metadata.setLastImport(OffsetDateTime.now());
            metadataRepository.save(metadata);

            log.info("Successfully finished import. Total cards processed: {}", totalProcessedCards);
        } else {
            log.info("Data is already up to date.");
        }
    }

    private void startStreamingImport(String url) {
        this.downloadClient = RestClient.builder()
                .baseUrl(url)
                .defaultHeader("User-Agent", "Mozilla/5.0")
                .build();

        runImport();
    }

    @Async
    public void runImport() {
        downloadClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .exchange((_, response) -> {
                    try (InputStream is = response.getBody()) {
                        InputStream processedStream = is;
                        String contentEncoding = response.getHeaders().getFirst(HttpHeaders.CONTENT_ENCODING);
                        if ("gzip".equalsIgnoreCase(contentEncoding)) {
                            log.info("GZIP encoding detected, decompressing stream...");
                            processedStream = new GZIPInputStream(is);
                        }

                        processJsonStream(processedStream);

                    } catch (Exception e) {
                        log.error("Fatal error during streaming import", e);
                        throw new RuntimeException("Fehler beim Streaming-Import", e);
                    }
                    return null;
                });
    }

    private void processJsonStream(InputStream is) {
        List<MagicImportData> batch = new ArrayList<>();
        int batchSize = 50;

        log.info("Caching sets for fast lookup...");
        Map<String, MagicSet> setCache = new HashMap<>();
        magicService.getAllSets().forEach(set -> setCache.put(set.getImportId(),set));
        log.info("{} sets cached.", setCache.size());

        MappingIterator<MagicImportData> iterator = objectMapper
                .readerFor(MagicImportData.class)
                .readValues(is);

        while (iterator.hasNextValue()) {
            var card = iterator.nextValue();
            batch.add(card);

            if (batch.size() >= batchSize) {
                saveBatch(batch,setCache);
                batch.clear();

                if (totalProcessedCards % 10000 == 0 && totalProcessedCards > 0) {
                    log.info("Progress: {} cards processed...", totalProcessedCards);
                }
            }
        }

        if (!batch.isEmpty()) {
            saveBatch(batch,setCache);
        }

        log.info("Import finished, processed {} cards.", totalProcessedCards);
    }

    private void saveBatch(List<MagicImportData> batch, Map<String, MagicSet> setCache) {
        List<MagicCard> entities = batch.stream()
                .filter(importData -> "card".equals(importData.object()))
                .map(importData -> {
                    MagicCard cardEntity = importData.basicCardEntity();

                    if (setCache.containsKey(importData.set_id())){
                        cardEntity.setSet(setCache.get(importData.set_id()));
                    } else {
                        var newSet = importData.basicSetEntity();
                        magicService.saveSet(newSet);
                        setCache.put(importData.set_id(), newSet);
                        cardEntity.setSet(newSet);
                        log.debug("Found and saved new set: {}", newSet.getName());
                    }
                    return cardEntity;
                })
                .toList();

        magicService.saveAll(entities);
        totalProcessedCards += entities.size();
    }


}
