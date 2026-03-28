package de.hitohitonika.tcgs.cardcollectionservice.magic.data.services;

import de.hitohitonika.tcgs.cardcollectionservice.data.GameType;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.projections.SetLookup;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.services.TcgService;
import de.hitohitonika.tcgs.cardcollectionservice.magic.data.entities.MagicCard;
import de.hitohitonika.tcgs.cardcollectionservice.magic.data.entities.MagicSet;
import de.hitohitonika.tcgs.cardcollectionservice.magic.data.repositories.MagicCardRepository;
import de.hitohitonika.tcgs.cardcollectionservice.magic.data.repositories.MagicSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MagicService implements TcgService<MagicCard,MagicCard> {
    private final MagicCardRepository magicCardRepository;
    private final MagicSetRepository magicSetRepository;

    public static final Map<String, String> SORT_MAPPING = Map.of(
            "name", "name",
            "rarity", "rarity"
    );

    @Override
    public GameType getGameType() {
        return GameType.MAGIC;
    }

    @Override
    public Optional<MagicCard> getPrint(String id){
        return magicCardRepository.findById(id);
    }

    @Override
    public Optional<MagicCard> getCard(String cardId) {
        return magicCardRepository.findById(cardId);
    }

    @Override
    public Page<MagicCard> getPrints(String nameLike, String type, String setId, int page, int size, String sortBy, String order) {
        return null;
    }

    @Override
    public List<SetLookup> getBasicSetInfo(){
        return magicSetRepository.findAllSetNamesAndIds();
    }

    public List<String> getCardTypes(){
        return magicCardRepository.findAllDistinctTypes();
    }

    @Override
    public List<String> getSortMappingKeys(){
        return new ArrayList<>(SORT_MAPPING.keySet());
    }

    public List<MagicSet> getAllSets() {
        return magicSetRepository.findAll();
    }

    public void saveSet(MagicSet magicSet) {
        magicSetRepository.save(magicSet);
    }

    @Transactional
    public void saveAll(List<MagicCard> cards) {
        magicCardRepository.saveAll(cards);
    }

}
