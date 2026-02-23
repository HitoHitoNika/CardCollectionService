package de.hitohitonika.tcgs.cardcollectionservice.data.db.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum GameTypes {
    YGO("Yu-Gi-Oh","ygo"),
    OP("OnePiece","op"),
    MAGIC("Magic","magic");

    public final String name;
    public final String key;

    GameTypes(String name, String key) {
        this.name = name;
        this.key = key;
    }

    @JsonCreator
    public static GameTypes fromString(String name) {
        for (GameTypes gameType : GameTypes.values()) {
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
