package de.hitohitonika.tcgs.cardcollectionservice.data.ygo.services;

import de.hitohitonika.tcgs.cardcollectionservice.data.ygo.entities.YgoSet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YgoSetRepository extends JpaRepository<YgoSet, Long> {}
