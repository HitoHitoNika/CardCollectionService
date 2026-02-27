package de.hitohitonika.tcgs.cardcollectionservice.importers;

/**
 * A DataImporter imports the data for its specific TCG.
 */
public interface DataImporter {
    /**
     * The core function of the DataImporter.
     * Its really important that this handles its own errors, since the Orchestrator won't care for it.
     */
    void importData();

    /**
     * Should check if the import already ran.
     * Most of the time, it's enough to check whether any entries have been created.
     *
     * @return true if the import ran
     */
    boolean didImportRun();
}
