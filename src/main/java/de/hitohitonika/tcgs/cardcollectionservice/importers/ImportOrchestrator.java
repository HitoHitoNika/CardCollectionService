package de.hitohitonika.tcgs.cardcollectionservice.importers;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImportOrchestrator {
    private final List<DataImporter> importers;

    public ImportOrchestrator(List<DataImporter> importers) {
        this.importers = importers;
    }

    /**
     * Check if ANY importer already ran
     *
     * @return true if an importer did already run
     */
    public boolean didImportAlreadyRun() {
        return importers.stream()
                .anyMatch(DataImporter::didImportRun);
    }

    /**
     * This will start each DataImporter in its own virtual thread.
     * If any importer does run into an issue, the ImportOrchestrator will NOT notice.
     * Each DataImporter should handle its own errors
     */
    public void runAllImports() {
        for (DataImporter importer : importers) {
            Thread.startVirtualThread(importer::importData);
        }
    }
}
