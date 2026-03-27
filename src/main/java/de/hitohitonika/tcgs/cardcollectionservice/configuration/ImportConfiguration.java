package de.hitohitonika.tcgs.cardcollectionservice.configuration;

import de.hitohitonika.tcgs.cardcollectionservice.data.GameType;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "app")
public record ImportConfiguration(Map<String, ImportDetail> imports) {
    public record ImportDetail(
            String address
    ) {}

    public List<GameType> getEnabled() {
        List<GameType> result = new ArrayList<>();

        for (GameType gameType : GameType.values()) {
            if (imports.containsKey(gameType.getKey())) {
                result.add(gameType);
            }
        }

        return result;
    }
}