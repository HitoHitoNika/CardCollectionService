package de.hitohitonika.tcgs.cardcollectionservice.data.db.repositories;

import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.OpSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OpSetRepository extends JpaRepository<OpSet, String>, JpaSpecificationExecutor<OpSet> {
}
