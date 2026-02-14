package de.hitohitonika.tcgs.cardcollectionservice.ygo.services;

import de.hitohitonika.tcgs.cardcollectionservice.ygo.entities.YgoSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "sets", collectionResourceRel = "sets")
public interface YgoSetRepository extends JpaRepository<YgoSet, Long> {}
