package de.hitohitonika.tcgs.cardcollectionservice.data.ygo.dtos;

import de.hitohitonika.tcgs.cardcollectionservice.data.ygo.entities.YgoCard;

import java.util.List;

public record YgoCardDto(
        Long id,
        String name,
        String type,
        String description,
        String archetype,
        String imageUrl
) {
    public static YgoCardDto fromEntity(YgoCard ygoCard) {
        return new YgoCardDto(
                ygoCard.getId(),
                ygoCard.getName(),
                ygoCard.getType(),
                ygoCard.getDescription(),
                ygoCard.getArchetype(),
                ygoCard.getImage()
        );
    }

    public static List<YgoCardDto> fromListOfEntities(List<YgoCard> ygoCards) {
        return ygoCards.stream().map(YgoCardDto::fromEntity).toList();
    }
}
