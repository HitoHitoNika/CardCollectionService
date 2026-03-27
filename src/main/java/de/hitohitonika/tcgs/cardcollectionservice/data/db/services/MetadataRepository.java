package de.hitohitonika.tcgs.cardcollectionservice.data.db.services;

import de.hitohitonika.tcgs.cardcollectionservice.data.db.ImportMetadata;
import org.springframework.data.repository.CrudRepository;

public interface MetadataRepository extends CrudRepository<ImportMetadata, String> {}
