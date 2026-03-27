package de.hitohitonika.tcgs.cardcollectionservice.magic.data.repositories;

import de.hitohitonika.tcgs.cardcollectionservice.data.db.projections.SetLookup;
import de.hitohitonika.tcgs.cardcollectionservice.magic.data.entities.MagicSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MagicSetRepository extends JpaRepository<MagicSet, String> {
    @Query("SELECT s.name as setName, s.importId as id FROM MagicSet s ORDER BY s.name ASC")
    List<SetLookup> findAllSetNamesAndIds();
}
