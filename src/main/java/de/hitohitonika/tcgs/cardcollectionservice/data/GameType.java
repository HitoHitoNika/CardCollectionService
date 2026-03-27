package de.hitohitonika.tcgs.cardcollectionservice.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum GameType {
    YGO("Yu-Gi-Oh","ygo"),
    OP("OnePiece","op"),
    MAGIC("Magic","magic");

    private final String name;
    private final String key;

    GameType(String name, String key) {
        this.name = name;
        this.key = key;
    }

    @JsonCreator
    public static GameType fromString(String name) {
        for (GameType gameType : GameType.values()) {
            if (gameType.name.equalsIgnoreCase(name) || gameType.key.equalsIgnoreCase(name)) {
                return gameType;
            }
        }
        throw new IllegalArgumentException("No such gameType " + name);
    }

    @JsonValue
    public String getKey() {
        return key;
    }


}
