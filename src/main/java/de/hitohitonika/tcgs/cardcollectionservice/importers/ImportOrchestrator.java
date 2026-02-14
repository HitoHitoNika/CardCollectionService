package de.hitohitonika.tcgs.cardcollectionservice.importers;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImportOrchestrator {
    private final List<DataImporter> importers;

    public ImportOrchestrator(List<DataImporter> importers) {
        this.importers = importers;
    }

    public boolean didImportAlreadyRun() {
        return importers.stream()
                .anyMatch(DataImporter::didImportRun);
    }

    public void runAllImports() {
        importers.forEach(DataImporter::importData);
    }
}
