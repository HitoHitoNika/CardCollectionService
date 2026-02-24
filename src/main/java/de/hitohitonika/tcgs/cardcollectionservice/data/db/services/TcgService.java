package de.hitohitonika.tcgs.cardcollectionservice.data.db.services;

import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.GameType;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.TCGPrint;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.projections.SetLookup;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TcgService<E extends TCGPrint> {
    GameType getGameType();
    Page<E> getPrints(String nameLike, String type, Long setId, int page, int size, String sortBy, String order);
    List<String> getCardTypes();
    List<String> getSortMappingKeys();
    List<SetLookup> getBasicSetInfo();
}
