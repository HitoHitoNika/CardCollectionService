package de.hitohitonika.tcgs.cardcollectionservice.data.general;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "app")
public record ApplicationConfiguration(Map<String, ImportDetail> imports) {
    public record ImportDetail(
            boolean enabled,
            String address
    ) {}

    public List<GameTypes> getEnabled() {
        List<GameTypes> result = new ArrayList<>();

        for (GameTypes gameType : GameTypes.values()) {
            if (imports.containsKey(gameType.key)) {
                var importInfo = imports.get(gameType.key);

                if (importInfo.enabled) {
                    result.add(gameType);
                }
            }
        }

        return result;
    }
}