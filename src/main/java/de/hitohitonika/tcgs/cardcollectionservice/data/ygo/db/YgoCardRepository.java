package de.hitohitonika.tcgs.cardcollectionservice.data.ygo.db;

import de.hitohitonika.tcgs.cardcollectionservice.data.ygo.entities.YgoCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface YgoCardRepository extends JpaRepository<YgoCard, Long>, JpaSpecificationExecutor<YgoCard> {
    @Query("SELECT DISTINCT c.type FROM YgoCard c WHERE c.type IS NOT NULL ORDER BY c.type ASC")
    List<String> findAllDistinctTypes();
}
