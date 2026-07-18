package de.hitohitonika.tcgs.cardcollectionservice.user.dto;


import de.hitohitonika.tcgs.cardcollectionservice.data.GameType;

import java.util.List;

public record RegisterRequest(String username, String password, List<GameType> games) {}
