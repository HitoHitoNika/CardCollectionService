package de.hitohitonika.tcgs.cardcollectionservice.ygo.services;

import de.hitohitonika.tcgs.cardcollectionservice.ygo.entities.YgoCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "cards", collectionResourceRel = "cards")
public interface YgoCardRepository extends JpaRepository<YgoCard, Long> {
    List<YgoCard> findByNameContaining(@Param("name") String name);
}
