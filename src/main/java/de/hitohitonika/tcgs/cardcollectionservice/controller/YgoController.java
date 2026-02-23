package de.hitohitonika.tcgs.cardcollectionservice.controller;

import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.YgoCard;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.YgoCardPrint;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.projections.YgoSetLookup;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.services.YgoService;
import de.hitohitonika.tcgs.cardcollectionservice.data.dtos.YgoCardDto;
import de.hitohitonika.tcgs.cardcollectionservice.data.dtos.YgoPrintDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("ygo")
public class YgoController {

    private final YgoService ygoService;

    public YgoController(YgoService ygoService) {
        this.ygoService = ygoService;
    }

    @GetMapping("cards")
    public ResponseEntity<List<YgoCardDto>> getCards(
            @RequestParam(name = "name-like", required = false) String nameLike,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long setId
    ) {
        List<YgoCard> cards = ygoService.getCards(nameLike, type, setId);
        return ResponseEntity.ok(YgoCardDto.fromListOfEntities(cards));
    }

    @GetMapping("prints")
    public ResponseEntity<Page<YgoPrintDto>> getPrints(
            @RequestParam(required = false) Long setId,
            @RequestParam(required = false) String type,
            @RequestParam(name = "name-like", required = false) String nameLike,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "setCode") String sortBy,
            @RequestParam(defaultValue = "asc") String order
    ) {
        Page<YgoCardPrint> printPage = ygoService.getPrints(nameLike, type, setId, page, size, sortBy, order);

        Page<YgoPrintDto> dtoPage = printPage.map(YgoPrintDto::fromEntity);

        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("sets/names")
    public ResponseEntity<List<YgoSetLookup>> getSetNames() {
        return ResponseEntity.ok(
          ygoService.getBasicSetInfo()
        );
    }

    @GetMapping("cards/types")
    public ResponseEntity<List<String>> getAllCardTypes() {
        return ResponseEntity.ok(ygoService.getCardTypes());
    }

    @GetMapping("prints/sortOptions")
    public ResponseEntity<List<String>> getSortOptions() {
        List<String> sortOptions = new ArrayList<>(YgoService.SORT_MAPPING.keySet());
        return ResponseEntity.ok(sortOptions);
    }
}
