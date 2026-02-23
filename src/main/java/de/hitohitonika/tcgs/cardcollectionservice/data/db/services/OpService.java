package de.hitohitonika.tcgs.cardcollectionservice.data.db.services;

import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.OpCard;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.OpSet;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.repositories.OpCardRepository;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.repositories.OpSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpService {
    private final OpSetRepository opSetRepository;

    private final OpCardRepository opCardRepository;

    @Transactional
    public OpSet saveIfNotExist(OpSet opSet) {
        if (!opSetRepository.existsById(opSet.getSetId())) {
            return opSetRepository.save(opSet);
        }
        return opSet;
    }

    @Transactional
    public void saveAll(List<OpCard> opCards) {
        Set<String> existingCards = opCardRepository.findAll()
                .stream()
                .map(opCard -> opCard.getCardCode() + opCard.getRarity())
                .collect(Collectors.toSet());

        List<OpCard> newCards = opCards.stream()
                .filter(c -> !existingCards.contains(c.getCardCode() + c.getRarity()))
                .toList();

        if (!newCards.isEmpty()) {
            opCardRepository.saveAll(newCards);
        }
    }
}
