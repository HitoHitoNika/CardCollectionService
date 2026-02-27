package de.hitohitonika.tcgs.cardcollectionservice.importers;

import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.OpCard;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.OpSet;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.services.OpService;
import de.hitohitonika.tcgs.cardcollectionservice.importers.rawdata.OPCardImportData;
import de.hitohitonika.tcgs.cardcollectionservice.importers.rawdata.OPSetImportData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
@ConditionalOnBooleanProperty(
        prefix = "app.imports.op",
        name = "enabled"
)
public class OpImporter implements DataImporter {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final RestClient restClient;

    private final OpService opService;

    OpImporter(@Value("${app.imports.op.address}") String address, OpService opService) {
        restClient = RestClient.builder()
                .baseUrl(address)
                .defaultHeader("User-Agent", "Mozilla/5.0")
                .build();

        this.opService = opService;
    }


    @Override
    public void importData() {
        log.info("Importing OP data...");

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var setFutures = Arrays.stream(SetEndpoint.values())
                    .map(e -> executor.submit(() -> fetchSets(e)))
                    .toList();

            List<OpSet> allSets = setFutures.stream()
                    .flatMap(f -> getFutureResult(f).stream())
                    .toList();

            Map<String, OpSet> setCache = allSets.stream()
                    .collect(Collectors.toMap(OpSet::getSetName, s -> s, (s1, _) -> s1));

            var cardFutures = Arrays.stream(CardEndpoint.values())
                    .map(e -> executor.submit(() -> fetchCards(e, setCache)))
                    .toList();

            List<OpCard> allCards = cardFutures.stream()
                    .flatMap(f -> getFutureResult(f).stream())
                    .toList();

            var stats = opService.importEverything(allSets, allCards);

            log.info("Import finished: {} new cards, {} sets processed.",
                    stats.get("newCards"), stats.get("newSets"));

        } catch (Exception e) {
            throw new ImportException("OP Import failed: " + e.getMessage());
        }
    }

    private List<OpSet> fetchSets(SetEndpoint endpoint) {
        var data = restClient.get().uri(endpoint.path).retrieve()
                .body(new ParameterizedTypeReference<List<OPSetImportData>>() {
                });
        return data == null ? List.of() : data.stream().map(OPSetImportData::toEntity).toList();
    }

    private List<OpCard> fetchCards(CardEndpoint endpoint, Map<String, OpSet> setCache) {
        var data = restClient.get().uri(endpoint.path).retrieve()
                .body(new ParameterizedTypeReference<List<OPCardImportData>>() {
                });
        if (data == null) return List.of();

        return data.stream().map(d -> {
            OpCard entity = OPCardImportData.toEntity(d);
            entity.setSet(setCache.get(d.set_name()));
            return entity;
        }).toList();
    }

    private <T> T getFutureResult(java.util.concurrent.Future<T> future) {
        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean didImportRun() {
        return opService.doEntriesExist();
    }

    private enum CardEndpoint {
        SETS("allSetCards/"),
        STARTER_DECKS("allSTCards/"),
        PROMOS("allPromos/");

        public final String path;

        CardEndpoint(String path) {
            this.path = path;
        }
    }

    private enum SetEndpoint {
        SETS("allSets/"),
        STARTER_DECKS("allDecks/");

        public final String path;

        SetEndpoint(String path) {
            this.path = path;
        }
    }
}
