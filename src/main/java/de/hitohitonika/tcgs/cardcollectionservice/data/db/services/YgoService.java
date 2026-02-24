package de.hitohitonika.tcgs.cardcollectionservice.data.db.services;

import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.YgoCard;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.YgoCardPrint;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.YgoSet;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.projections.YgoSetLookup;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.repositories.YgoCardPrintRepository;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.repositories.YgoCardRepository;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.repositories.YgoSetRepository;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.specifications.YgoCardPrintSpecifications;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.specifications.YgoCardSpecifications;
import de.hitohitonika.tcgs.cardcollectionservice.importers.rawdata.YgoImportData;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class YgoService {

    public static final Map<String, String> SORT_MAPPING = Map.of(
            "name", "originalCard.name",
            "setName", "set.setName",
            "rarity", "rarity",
            "setCode", "set.setCode"
    );

    private final Logger log = LoggerFactory.getLogger(YgoService.class);

    private final YgoCardRepository ygoCardRepository;

    private final YgoSetRepository ygoSetRepository;

    private final YgoCardPrintRepository ygoCardPrintRepository;

    public boolean doEntriesExist() {
        return ygoCardRepository.count() > 0;
    }

    public List<YgoCard> getCards(String name, String type, Long setId) {
        Specification<YgoCard> spec = Specification
                .where(YgoCardSpecifications.hasNameLike(name))
                .and(YgoCardSpecifications.hasType(type))
                .and(YgoCardSpecifications.isInSet(setId));

        return ygoCardRepository.findAll(spec);
    }

    public Page<YgoCardPrint> getPrints(String name, String type, Long setId, int page, int size, String sortBy, String sortDirection) {
        String jpaField = SORT_MAPPING.getOrDefault(sortBy, "set.setCode");
        Sort.Direction dir = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(dir, jpaField).and(Sort.by(Sort.Direction.ASC, "cardNumber")));

        Specification<YgoCardPrint> spec = (root, query, cb) -> {
            if (query.getResultType() != Long.class) {
                root.fetch("originalCard", JoinType.LEFT);
                root.fetch("set", JoinType.LEFT);
            }

            // Kombiniere die restlichen Bedingungen
            return Specification.where(YgoCardPrintSpecifications.hasCardNameLike(name))
                    .and(YgoCardPrintSpecifications.hasCardType(type))
                    .and(YgoCardPrintSpecifications.hasSetId(setId))
                    .toPredicate(root, query, cb);
        };

        return ygoCardPrintRepository.findAll(spec, pageable);
    }

    /**
     * Sucht alle Sets in alphabetischer Reihenfolge
     * @return YgoSetLookup Liste mit Set Name und Set Id
     */
    public List<YgoSetLookup> getBasicSetInfo() {
        return ygoSetRepository.findAllSetNamesAndIds();
    }

    public List<String> getCardTypes(){
        return ygoCardRepository.findAllDistinctTypes();
    }

    @Transactional
    public void importData(YgoImportData importData) {
        Map<String, YgoSet> setCache = ygoSetRepository.findAll()
                .stream()
                .collect(Collectors.toMap(YgoSet::getSetName, s -> s));

        Set<String> existingCardNames = new HashSet<>(ygoCardRepository.findAllCardNames());

        AtomicInteger cardsCreated = new AtomicInteger(0);
        AtomicInteger printsAdded = new AtomicInteger(0);
        AtomicInteger setsCreated = new AtomicInteger(0);

        List<YgoCard> newCards = new ArrayList<>();
        List<YgoSet> setsToSave = new ArrayList<>();

        importData.data().forEach(rawCard -> {
            if (rawCard.card_sets() != null) {
                for (var rawSet : rawCard.card_sets()) {
                    if (!setCache.containsKey(rawSet.set_name())) {
                        String code = rawSet.set_code().contains("-") ? rawSet.set_code().split("-")[0] : "UNKNOWN";
                        YgoSet newSet = rawSet.basicYgoSet(code);
                        setCache.put(newSet.getSetName(), newSet);
                        setsToSave.add(newSet);
                        setsCreated.incrementAndGet();
                    }
                }
            }
        });

        if (!setsToSave.isEmpty()) ygoSetRepository.saveAll(setsToSave);

        importData.data().forEach(rawCard -> {
            YgoCard card;
            boolean isNew = !existingCardNames.contains(rawCard.name());

            if (isNew) {
                card = rawCard.basicYgoCard();
                cardsCreated.incrementAndGet();
                newCards.add(card);
            } else {
                card = ygoCardRepository.findByNameWithPrints(rawCard.name())
                        .orElse(rawCard.basicYgoCard());
            }

            if (rawCard.card_sets() != null) {
                for (var rawSet : rawCard.card_sets()) {
                    YgoSet set = setCache.get(rawSet.set_name());
                    var newPrint = rawSet.basicYgoCardPrint();

                    // Print-Duplikate checken
                    if (card.getPrints().stream().noneMatch(p -> p.getCardNumber().equals(newPrint.getCardNumber()))) {
                        newPrint.setSet(set);
                        card.addPrint(newPrint);
                        printsAdded.incrementAndGet();
                    }
                }
            }
        });

        ygoCardRepository.saveAll(newCards);

        log.info("Import finished: {} new cards, {} new prints, {} new sets created.",
                cardsCreated.get(), printsAdded.get(), setsCreated.get());
    }
}
