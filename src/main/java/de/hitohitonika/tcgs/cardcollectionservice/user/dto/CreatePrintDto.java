package de.hitohitonika.tcgs.cardcollectionservice.user.dto;

import de.hitohitonika.tcgs.cardcollectionservice.data.GameType;
import de.hitohitonika.tcgs.cardcollectionservice.user.db.AppUserPrint;
import de.hitohitonika.tcgs.cardcollectionservice.user.db.CardCondition;

public record CreatePrintDto(GameType gameType, String printId, int amount, String description, CardCondition cardCondition) {
    public AppUserPrint toEntity() {
        var appUserPrint = new AppUserPrint();
        appUserPrint.setGameType(gameType);
        appUserPrint.setPrintId(printId);
        appUserPrint.setAmount(amount);
        appUserPrint.setDescription(description);
        appUserPrint.setCondition(cardCondition);
        return appUserPrint;
    }
}
