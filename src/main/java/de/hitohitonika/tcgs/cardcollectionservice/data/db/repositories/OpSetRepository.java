package de.hitohitonika.tcgs.cardcollectionservice.data.db.repositories;

import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.OpSet;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.projections.SetLookup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OpSetRepository extends JpaRepository<OpSet, String>, JpaSpecificationExecutor<OpSet> {
    @Query("SELECT s.id as id, s.setName as setName FROM OpSet s ORDER BY s.setName ASC")
    List<SetLookup> findAllSetNamesAndIds();
}
