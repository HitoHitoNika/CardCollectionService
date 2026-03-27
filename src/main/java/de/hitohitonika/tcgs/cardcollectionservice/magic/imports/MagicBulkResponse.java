package de.hitohitonika.tcgs.cardcollectionservice.magic.imports;

import java.util.List;

/**
 * The Scryfall API starts with /bulk-data
 * This Endpoint basically provides a definition on what we can import
 * While we technically want the complete Data,
 * one could decide to load for example only English Data
 */
public record MagicBulkResponse(String object, boolean has_more, List<MagicBulkEndpointDefinition> data) {
}
