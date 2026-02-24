package de.hitohitonika.tcgs.cardcollectionservice.data.db.repositories;

import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.YgoSet;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.projections.SetLookup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface YgoSetRepository extends JpaRepository<YgoSet, Long> {
    @Query("SELECT s.id as id, s.setName as setName FROM YgoSet s ORDER BY s.setName ASC")
    List<SetLookup> findAllSetNamesAndIds();
}
