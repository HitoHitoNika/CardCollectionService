package de.hitohitonika.tcgs.cardcollectionservice.data.db.projections;

/**
 * Lookup to fetch basic set information
 */
public interface SetLookup {
    Long getId();
    String getSetName();
}
