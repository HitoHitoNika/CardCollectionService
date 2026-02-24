package de.hitohitonika.tcgs.cardcollectionservice.data.db.repositories;

import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.OpCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface OpCardRepository extends JpaRepository<OpCard, Long>, JpaSpecificationExecutor<OpCard> {
    @Query("SELECT concat(c.cardCode, '_', c.name, '_', c.rarity) FROM OpCard c")
    Set<String> findAllCardCompositeKeys();
}
