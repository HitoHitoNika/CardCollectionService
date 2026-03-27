package de.hitohitonika.tcgs.cardcollectionservice.user.dto;

import de.hitohitonika.tcgs.cardcollectionservice.data.TcgPrint;
import de.hitohitonika.tcgs.cardcollectionservice.data.dtos.TcgPrintDto;
import de.hitohitonika.tcgs.cardcollectionservice.user.db.AppUserPrint;

public record AppUserPrintDto(
        long id, long ownerId, String gameType,
        TcgPrintDto printData, String condition,
        String description, int amount
) {
    public static AppUserPrintDto fromEntity(AppUserPrint appUserPrint, TcgPrint cardPrint) {
        return new AppUserPrintDto(
                appUserPrint.getId(), appUserPrint.getUser().getId(), appUserPrint.getGameType().getKey(),
                cardPrint.toPrintDto(), appUserPrint.getCondition().getValue(),
                appUserPrint.getDescription(), appUserPrint.getAmount()
        );
    }
}
