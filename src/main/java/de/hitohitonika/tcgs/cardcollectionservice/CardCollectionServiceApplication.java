package de.hitohitonika.tcgs.cardcollectionservice;

import de.hitohitonika.tcgs.cardcollectionservice.data.configuration.ApplicationConfiguration;
import de.hitohitonika.tcgs.cardcollectionservice.importers.ImportOrchestrator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfiguration.class)
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
