package de.hitohitonika.tcgs.cardcollectionservice.importers;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImportOrchestrator {
    private final List<DataImporter> importers;

    public ImportOrchestrator(List<DataImporter> importers) {
        this.importers = importers;
        IO.println(importers);
    }

    public void runAllImports() {
        importers.forEach(DataImporter::importData);
    }

    public void runSpecificImport(DataTypes category) {
        importers.stream()
                .filter(importer -> importer.supports(category))
                .findFirst()
                .ifPresent(DataImporter::importData);
    }
}
