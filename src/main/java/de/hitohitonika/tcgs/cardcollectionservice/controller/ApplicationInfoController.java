package de.hitohitonika.tcgs.cardcollectionservice.controller;

import de.hitohitonika.tcgs.cardcollectionservice.configuration.ImportConfiguration;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.GameType;
import de.hitohitonika.tcgs.cardcollectionservice.user.db.CardCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("info")
@RequiredArgsConstructor
public class ApplicationInfoController {

    private final ImportConfiguration config;

    @GetMapping("availableGames")
    public ResponseEntity<List<GameType>> getAvailableGames() {
        return ResponseEntity.ok(config.getEnabled());
    }

    @GetMapping("availableConditions")
    public ResponseEntity<List<CardCondition>> getAvailableConditions() {
        return ResponseEntity.ok(List.of(CardCondition.values()));
    }
}
