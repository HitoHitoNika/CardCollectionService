package de.hitohitonika.tcgs.cardcollectionservice.data.db.services;

import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.GameType;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.TcgCard;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.TcgPrint;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.projections.SetLookup;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface TcgService<C extends TcgCard,P extends TcgPrint> {
    GameType getGameType();
    Page<P> getPrints(String nameLike, String type, Long setId, int page, int size, String sortBy, String order);
    List<String> getCardTypes();
    List<String> getSortMappingKeys();
    List<SetLookup> getBasicSetInfo();
    Optional<C> getCard(long id);
    Optional<P> getPrint(long id);
}
