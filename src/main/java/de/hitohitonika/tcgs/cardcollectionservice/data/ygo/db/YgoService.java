package de.hitohitonika.tcgs.cardcollectionservice.data.ygo.db;

import de.hitohitonika.tcgs.cardcollectionservice.data.ygo.db.projections.YgoSetLookup;
import de.hitohitonika.tcgs.cardcollectionservice.data.ygo.db.specifications.YgoCardPrintSpecifications;
import de.hitohitonika.tcgs.cardcollectionservice.data.ygo.db.specifications.YgoCardSpecifications;
import de.hitohitonika.tcgs.cardcollectionservice.data.ygo.entities.YgoCardPrint;
import de.hitohitonika.tcgs.cardcollectionservice.importers.YgoImportData;
import de.hitohitonika.tcgs.cardcollectionservice.data.ygo.entities.YgoCard;
import de.hitohitonika.tcgs.cardcollectionservice.data.ygo.entities.YgoSet;
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

import java.util.List;
import java.util.Map;
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
                .stream().collect(Collectors.toMap(YgoSet::getSetCode, s -> s));

        AtomicInteger newEntries = new AtomicInteger(0);

        importData.data().forEach(rawCard -> {
            var card = rawCard.basicYgoCard();

            if (rawCard.card_sets() != null) {
                rawCard.card_sets().forEach(rawSet -> {
                    String setName = rawSet.set_name();
                    YgoSet set = setCache.get(setName);

                    if (set == null) {
                        String determinedCode = "BadData-UNKNOWN";

                        if (rawSet.set_code().contains("-")) {
                            determinedCode = rawSet.set_code().split("-")[0];
                        } else {
                            log.info("Bad Entry found [{}]", rawSet.set_code());
                        }

                        final String finalCode = determinedCode;
                        set = setCache.computeIfAbsent(setName, _ -> {
                            var newSet = rawSet.basicYgoSet(finalCode);
                            newEntries.getAndIncrement();
                            return ygoSetRepository.save(newSet);
                        });
                    }

                    if (set.getSetCode().startsWith("BadData") && rawSet.set_code().contains("-")) {
                        set.setSetCode(rawSet.set_code().split("-")[0]);
                        ygoSetRepository.save(set);
                    }

                    var print = rawSet.basicYgoCardPrint();

                    if(print.getCardNumber().equalsIgnoreCase(rawSet.set_code())){
                        log.warn("Invalid set_code found [{}]", print.getCardNumber());
                    }

                    print.setSet(set);
                    card.addPrint(print);
                });
            }
            newEntries.getAndIncrement();
            ygoCardRepository.save(card);
        });

        log.info("Finished importing [{}] Yugioh Data Entries!",newEntries.get());
    }
}
