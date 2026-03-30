package de.hitohitonika.tcgs.cardcollectionservice.data.db.services;

import de.hitohitonika.tcgs.cardcollectionservice.data.GameType;
import de.hitohitonika.tcgs.cardcollectionservice.data.TcgCard;
import de.hitohitonika.tcgs.cardcollectionservice.data.TcgPrint;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class TcgServiceHelper {
    private final Map<GameType, TcgService<? extends TcgCard, ? extends TcgPrint>> serviceMap = new EnumMap<>(GameType.class);

    private final List<TcgService<? extends TcgCard, ? extends TcgPrint>> availableServices;

    @PostConstruct
    private void init() {
        for (var service : availableServices) {
            serviceMap.put(service.getGameType(), service);
        }
    }

    public TcgService<? extends TcgCard, ? extends TcgPrint> getService(GameType gameType) {
        var service = serviceMap.get(gameType);
        if (service == null) {
            throw new IllegalArgumentException("No Service for TCG Type found: " + gameType);
        }
        return service;
    }
}
