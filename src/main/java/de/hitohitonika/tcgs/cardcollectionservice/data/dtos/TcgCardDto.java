package de.hitohitonika.tcgs.cardcollectionservice.data.dtos;

import java.util.List;
import java.util.Map;

public record TcgCardDto (
    String name,
    String description,
    String archetype,
    String cardType,
    long cardId,
    String imageUrl,
    List<TcgPrintDto> prints,
    Map<String,String> customData
){}
