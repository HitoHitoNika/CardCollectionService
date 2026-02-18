package de.hitohitonika.tcgs.cardcollectionservice.data.ygo.controller;

import de.hitohitonika.tcgs.cardcollectionservice.data.ygo.dtos.YgoCardDto;
import de.hitohitonika.tcgs.cardcollectionservice.data.ygo.services.YgoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("ygo")
public class YgoController {

    private final YgoService ygoService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public YgoController(YgoService ygoService) {
        this.ygoService = ygoService;
    }

    @GetMapping("cards")
    public ResponseEntity<List<YgoCardDto>> getCards() {
        return ResponseEntity.ok(
                YgoCardDto.fromListOfEntities(ygoService.getCards())
        );
    }
}
