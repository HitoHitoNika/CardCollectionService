package de.hitohitonika.tcgs.cardcollectionservice.magic.data.services;

import de.hitohitonika.tcgs.cardcollectionservice.data.db.projections.SetLookup;
import de.hitohitonika.tcgs.cardcollectionservice.magic.data.entities.MagicCard;
import de.hitohitonika.tcgs.cardcollectionservice.magic.data.repositories.MagicCardRepository;
import de.hitohitonika.tcgs.cardcollectionservice.magic.data.repositories.MagicSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MagicCardService {
    private final MagicCardRepository magicCardRepository;
    private final MagicSetRepository magicSetRepository;

    public static final Map<String, String> SORT_MAPPING = Map.of(
            "name", "name",
            "rarity", "rarity"
    );

    public Optional<MagicCard> getCard(String cardId) {
        return magicCardRepository.findById(cardId);
    }

    public List<SetLookup> getSetInfo(){
        return magicSetRepository.findAllSetNamesAndIds();
    }

    public List<String> getCardTypes(){
        return magicCardRepository.findAllDistinctTypes();
    }

    public Set<String> getSortOptions(){
        return SORT_MAPPING.keySet();
    }

    @Transactional
    public void saveAll(List<MagicCard> cards) {
        magicCardRepository.saveAll(cards);
    }

}
