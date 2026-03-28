package de.hitohitonika.tcgs.cardcollectionservice.ygo.data.repositories;

import de.hitohitonika.tcgs.cardcollectionservice.ygo.data.entities.YgoCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface YgoCardRepository extends JpaRepository<YgoCard, String>, JpaSpecificationExecutor<YgoCard> {
    @Query("SELECT DISTINCT c.type FROM YgoCard c WHERE c.type IS NOT NULL ORDER BY c.type ASC")
    List<String> findAllDistinctTypes();

    @Query("SELECT c.name FROM YgoCard c")
    List<String> findAllCardNames();

    @Query("SELECT c FROM YgoCard c LEFT JOIN FETCH c.prints WHERE c.name = :name")
    Optional<YgoCard> findByNameWithPrints(@Param("name") String name);
}
