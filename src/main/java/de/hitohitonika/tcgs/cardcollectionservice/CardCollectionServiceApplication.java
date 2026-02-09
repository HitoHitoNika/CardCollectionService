package de.hitohitonika.tcgs.cardcollectionservice;

import de.hitohitonika.tcgs.cardcollectionservice.importers.DataImporter;
import de.hitohitonika.tcgs.cardcollectionservice.importers.ImportOrchestrator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CardCollectionServiceApplication {

     static void main(String[] args) {
        var context = SpringApplication.run(CardCollectionServiceApplication.class, args);

        var orchestrator = context.getBean(ImportOrchestrator.class);

        orchestrator.runAllImports();
    }

}
