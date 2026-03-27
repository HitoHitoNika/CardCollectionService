package de.hitohitonika.tcgs.cardcollectionservice.magic.imports;

import java.time.OffsetDateTime;

public record MagicBulkEndpointDefinition(
        String object,
        String id,
        String type,
        OffsetDateTime updated_at,
        String uri,
        String name,
        String description,
        long size,
        String download_uri,
        String content_type,
        String content_encoding
) {}
