package de.hitohitonika.tcgs.cardcollectionservice.data.ygo.services;

import de.hitohitonika.tcgs.cardcollectionservice.importers.YgoImportData;
import de.hitohitonika.tcgs.cardcollectionservice.data.ygo.entities.YgoCard;
import de.hitohitonika.tcgs.cardcollectionservice.data.ygo.entities.YgoSet;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class YgoService {

    private final Logger log = LoggerFactory.getLogger(YgoService.class);

    private final YgoCardRepository ygoCardRepository;

    private final YgoSetRepository ygoSetRepository;

    public boolean doEntriesExist() {
        return ygoCardRepository.count() > 0;
    }

    public List<YgoCard> getCards() {
        return ygoCardRepository.findAll();
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
