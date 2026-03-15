package de.hitohitonika.tcgs.cardcollectionservice.user.db;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CardCondition {
    MINT("Mint"),
    NEAR_MINT("Near Mint"),
    EXCELLENT("Excellent"),
    GOOD("Good"),
    LIGHT_PLAYED("Light Played"),
    PLAYED("Played"),
    POOR("Poor");

    private final String value;

    CardCondition(String value) {
        this.value = value;
    }

    @JsonCreator
    public static CardCondition fromValue(String value) {
        for (CardCondition cardCondition : CardCondition.values()) {
            if (cardCondition.getValue().equalsIgnoreCase(value)) {
                return cardCondition;
            }
        }
        throw new IllegalArgumentException("Invalid value " + value);
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
