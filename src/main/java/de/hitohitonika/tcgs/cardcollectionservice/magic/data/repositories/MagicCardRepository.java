package de.hitohitonika.tcgs.cardcollectionservice.magic.data.repositories;

import de.hitohitonika.tcgs.cardcollectionservice.magic.data.entities.MagicCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MagicCardRepository extends JpaRepository<MagicCard, String>, JpaSpecificationExecutor<MagicCard> {
    @Query("SELECT DISTINCT c.type FROM MagicCard c WHERE c.type IS NOT NULL ORDER BY c.type ASC")
    List<String> findAllDistinctTypes();
}
