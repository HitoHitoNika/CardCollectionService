package de.hitohitonika.tcgs.cardcollectionservice.data.ygo.dtos;

import de.hitohitonika.tcgs.cardcollectionservice.data.ygo.entities.YgoCardPrint;

/**
 * YgoCardPrint angereichert mit Informationen aus dazugehörigen Karte / Set
 */
public record YgoPrintDto (
        String name,
        String setName,
        String imageUrl,
        String rarity,
        String setCode,
        String setNumber
){

    public static YgoPrintDto fromEntity(YgoCardPrint entity) {
        return new YgoPrintDto(
                entity.getOriginalCard().getName(),
                entity.getSet().getSetName(),
                entity.getOriginalCard().getImage(),
                entity.getRarity(),
                entity.getSet().getSetCode(),
                entity.getCardNumber()
        );
    }

}
