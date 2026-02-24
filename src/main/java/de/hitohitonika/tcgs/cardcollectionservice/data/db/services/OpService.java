package de.hitohitonika.tcgs.cardcollectionservice.data.db.services;

import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.OpCard;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.OpSet;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.repositories.OpCardRepository;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.repositories.OpSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OpService {
    private final OpSetRepository opSetRepository;

    private final OpCardRepository opCardRepository;

    @Transactional
    public Map<String, Integer> importEverything(List<OpSet> sets, List<OpCard> cards) {
        opSetRepository.saveAll(sets.stream().distinct().toList());

        Set<String> seenInThisRun = new HashSet<>(opCardRepository.findAllCardCompositeKeys());

        List<OpCard> newCards = new ArrayList<>();

        for (OpCard c : cards) {
            // TRIPLE-KEY für maximale Sicherheit
            String key = (c.getCardCode() + "_" + c.getName() + "_" + c.getRarity()).toLowerCase();

            if (!seenInThisRun.contains(key)) {
                newCards.add(c);
                seenInThisRun.add(key);
            }
        }

        if (!newCards.isEmpty()) {
            opCardRepository.saveAll(newCards);
        }

        return Map.of("newCards", newCards.size(), "newSets", sets.size());
    }
}
