package de.hitohitonika.tcgs.cardcollectionservice.data.db;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@NoArgsConstructor
@Data
public class ImportMetadata {
    @Id
    private String gameKey;

    private OffsetDateTime lastApiUpdate;

    private OffsetDateTime lastImport;

    public ImportMetadata(String gameKey, OffsetDateTime lastApiUpdate) {
        this.gameKey = gameKey;
        this.lastApiUpdate = lastApiUpdate;
    }
}
