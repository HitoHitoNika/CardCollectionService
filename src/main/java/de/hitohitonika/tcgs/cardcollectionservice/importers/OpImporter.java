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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public void importData() throws ImportException {
        log.info("Importing op data...");

        Map<String, OpSet> knownSets;

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var futures = Stream.of(SetEndpoint.values())
                    .map(endpoint -> executor.submit(() -> importSets(endpoint)))
                    .toList();

            knownSets = futures.stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            throw new RuntimeException("Fehler beim Import eines Endpoints", e);
                        }
                    })
                    .flatMap(List::stream)
                    .collect(Collectors.toMap(
                            OpSet::getSetName,
                            opSet -> opSet,
                            (existing, _) -> existing
                    ));
        }

        var newCards = new AtomicInteger(0);
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            var futures = Arrays.stream(CardEndpoint.values())
                    .map(cardEndpoint -> executor.submit(() -> importCards(cardEndpoint, knownSets, newCards)))
                    .toList();

            var fetchedCards = futures.stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            log.error("Fehler beim Import eines Cards", e);
                            throw new RuntimeException("Fehler beim Import eines Endpoints", e);
                        }
                    })
                    .flatMap(List::stream)
                    .toList();

            opService.saveAll(fetchedCards);

        } catch (RuntimeException e) {
            throw new ImportException("Der parallele Import ist fehlgeschlagen: " + e.getMessage());
        }

        log.info("Successfully imported {} op cards", newCards.get());
    }

    @Override
    public boolean didImportRun() {
        return false;
    }

    private List<OpSet> importSets(SetEndpoint setEndpoint) throws ImportException {
        var importData = this.restClient.get()
                .uri(setEndpoint.path)
                .retrieve()
                .body(new ParameterizedTypeReference<List<OPSetImportData>>() {
                });

        if (importData == null) {
            log.error("Endpoint {} returned null body", setEndpoint.path);
            throw new ImportException("Fehler beim Import eines Endpoints");
        }

        return importData.stream()
                .map(OPSetImportData::toEntity)
                .map(opService::saveIfNotExist)
                .toList();
    }

    private List<OpCard> importCards(CardEndpoint cardEndpoint, Map<String, OpSet> knownSets, AtomicInteger counter) throws ImportException {
        var importData = this.restClient.get()
                .uri(cardEndpoint.path)
                .retrieve()
                .body(new ParameterizedTypeReference<List<OPCardImportData>>() {
                });

        if (importData == null) {
            log.error("Endpoint {} returned null body for cards", cardEndpoint.path);
            throw new ImportException("Fehler beim Import eines Endpoints");
        }

        return importData.stream()
                .map(importedCard -> {
                    var opCard = OPCardImportData.toEntity(importedCard);

                    opCard.setSet(knownSets.get(importedCard.set_name()));
                    counter.getAndIncrement();
                    return opCard;
                })
                .toList();
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
