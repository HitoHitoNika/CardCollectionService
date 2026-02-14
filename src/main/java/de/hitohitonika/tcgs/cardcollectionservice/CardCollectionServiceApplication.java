package de.hitohitonika.tcgs.cardcollectionservice;

import de.hitohitonika.tcgs.cardcollectionservice.importers.ImportOrchestrator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CardCollectionServiceApplication {

     static void main(String[] args) {
        var context = SpringApplication.run(CardCollectionServiceApplication.class, args);

         var orchestrator = context.getBean(ImportOrchestrator.class);

         //Wenn Datensätze exisitieren sollten bei einem Neustart keine neuen importiert werden
         if(orchestrator.didImportAlreadyRun()){
            orchestrator.runAllImports();
         }

    }

}
