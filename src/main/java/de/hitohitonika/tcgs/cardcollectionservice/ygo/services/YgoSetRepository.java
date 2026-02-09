package de.hitohitonika.tcgs.cardcollectionservice.ygo.services;

import de.hitohitonika.tcgs.cardcollectionservice.ygo.entities.YgoSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface YgoSetRepository extends JpaRepository<YgoSet, Long> {}
