package de.hitohitonika.tcgs.cardcollectionservice.importers;

public interface DataImporter {
    void importData() throws ImportException;
    boolean didImportRun();
}
