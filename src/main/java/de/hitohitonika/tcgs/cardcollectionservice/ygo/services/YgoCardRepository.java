package de.hitohitonika.tcgs.cardcollectionservice.ygo.services;

import de.hitohitonika.tcgs.cardcollectionservice.ygo.entities.YgoCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YgoCardRepository extends JpaRepository<YgoCard, Long> {}
