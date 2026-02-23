package de.hitohitonika.tcgs.cardcollectionservice.data.db.repositories;

import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.OpCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OpCardRepository extends JpaRepository<OpCard, Long>, JpaSpecificationExecutor<OpCard> {
}
