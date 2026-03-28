package de.hitohitonika.tcgs.cardcollectionservice.data.db.services;

import de.hitohitonika.tcgs.cardcollectionservice.data.GameType;
import de.hitohitonika.tcgs.cardcollectionservice.data.TcgCard;
import de.hitohitonika.tcgs.cardcollectionservice.data.TcgPrint;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.projections.SetLookup;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface TcgService<C extends TcgCard,P extends TcgPrint> {
    GameType getGameType();
    Page<P> getPrints(String nameLike, String type, String setId, int page, int size, String sortBy, String order);
    List<String> getCardTypes();
    List<String> getSortMappingKeys();
    List<SetLookup> getBasicSetInfo();
    Optional<C> getCard(String id);
    Optional<P> getPrint(String id);
}
