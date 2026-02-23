package de.hitohitonika.tcgs.cardcollectionservice.importers.rawdata;

import com.fasterxml.jackson.annotation.JsonAlias;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.OpSet;

public record OPSetImportData(
        @JsonAlias({"set_name", "structure_deck_name"})
        String set_name,
        @JsonAlias({"set_id", "structure_deck_id"})
        String set_id
) {
    public static OpSet toEntity(OPSetImportData opSetImportData) {
        var opSet = new OpSet();

        opSet.setSetName(opSetImportData.set_name);
        opSet.setSetId(opSetImportData.set_id);

        return opSet;
    }
}
