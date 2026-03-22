package de.hitohitonika.tcgs.cardcollectionservice.data.dtos;

public record TcgPrintDto (
        String name,
        String setName,
        String imageUrl,
        String rarity,
        String cardCode,
        long cardId,
        long printId,
        String gameKey
) {}
