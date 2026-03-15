package de.hitohitonika.tcgs.cardcollectionservice.ygo.data.repositories;

import de.hitohitonika.tcgs.cardcollectionservice.ygo.data.entities.YgoCardPrint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface YgoCardPrintRepository extends JpaRepository<YgoCardPrint, Long>, JpaSpecificationExecutor<YgoCardPrint> {
}
