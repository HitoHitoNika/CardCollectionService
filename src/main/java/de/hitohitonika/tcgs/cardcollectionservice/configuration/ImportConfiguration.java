package de.hitohitonika.tcgs.cardcollectionservice.configuration;

import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.GameType;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "app")
public record ImportConfiguration(Map<String, ImportDetail> imports) {
    public record ImportDetail(
            boolean enabled,
            String address
    ) {}

    public List<GameType> getEnabled() {
        List<GameType> result = new ArrayList<>();

        for (GameType gameType : GameType.values()) {
            if (imports.containsKey(gameType.getKey())) {
                var importInfo = imports.get(gameType.getKey());

                if (importInfo.enabled) {
                    result.add(gameType);
                }
            }
        }

        return result;
    }
}