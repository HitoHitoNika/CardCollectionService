package de.hitohitonika.tcgs.cardcollectionservice.data.ygo.db;

import de.hitohitonika.tcgs.cardcollectionservice.data.ygo.entities.YgoCardPrint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface YgoCardPrintRepository extends JpaRepository<YgoCardPrint, Long>, JpaSpecificationExecutor<YgoCardPrint> {
}
