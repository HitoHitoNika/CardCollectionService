package de.hitohitonika.tcgs.cardcollectionservice.user.dto;

import de.hitohitonika.tcgs.cardcollectionservice.user.db.AppUser;

import java.util.List;

public record AppUserDto(String username, List<AppUserPrintDto> prints) {
    public static AppUserDto fromEntity(AppUser appUser, List<AppUserPrintDto> prints) {
        return new AppUserDto(appUser.getUsername(), prints);
    }
}
