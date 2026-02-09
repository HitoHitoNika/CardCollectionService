package de.hitohitonika.tcgs.cardcollectionservice.ygo.controller;

import de.hitohitonika.tcgs.cardcollectionservice.ygo.entities.YgoCard;
import de.hitohitonika.tcgs.cardcollectionservice.ygo.services.YgoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/ygo")
public class YgoController {

    private final YgoService ygoService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public YgoController(YgoService ygoService) {
        this.ygoService = ygoService;
    }

    @GetMapping("/cards")
    public ResponseEntity<List<YgoCard>> getCards() {
        return ResponseEntity.ok(ygoService.getCards());
    }
}
