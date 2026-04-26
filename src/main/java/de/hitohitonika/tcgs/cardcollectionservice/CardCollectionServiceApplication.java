package de.hitohitonika.tcgs.cardcollectionservice;

import de.hitohitonika.tcgs.cardcollectionservice.configuration.ImportConfiguration;
import de.hitohitonika.tcgs.cardcollectionservice.importers.ImportOrchestrator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(ImportConfiguration.class)
@EnableScheduling
public class CardCollectionServiceApplication {

     static void main(String[] args) {
        var context = SpringApplication.run(CardCollectionServiceApplication.class, args);

         var orchestrator = context.getBean(ImportOrchestrator.class);

         //When data already exists the import shouldn't run on startup
         if(!orchestrator.didImportAlreadyRun()){
            orchestrator.runAllImports();
         }

    }
}
