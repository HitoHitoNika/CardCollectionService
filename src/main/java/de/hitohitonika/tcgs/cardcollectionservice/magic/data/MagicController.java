package de.hitohitonika.tcgs.cardcollectionservice.magic.data;

import de.hitohitonika.tcgs.cardcollectionservice.data.db.projections.SetLookup;
import de.hitohitonika.tcgs.cardcollectionservice.magic.data.entities.MagicCard;
import de.hitohitonika.tcgs.cardcollectionservice.magic.data.services.MagicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("magic")
@RequiredArgsConstructor
public class MagicController {
    private final MagicService magicService;

    @GetMapping("cards/{cardId}")
    public ResponseEntity<MagicCard> getCard(@PathVariable String cardId){
        var card = magicService.getCard(cardId);

        return card.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("cards")
    public ResponseEntity<Page<MagicCard>> getCards(
            @RequestParam(required = false) String setId,
            @RequestParam(required = false) String type,
            @RequestParam(name = "name-like", required = false) String nameLike,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "setCode") String sortBy,
            @RequestParam(defaultValue = "asc") String order
    ){
        return ResponseEntity.ok().build();
    }

    @GetMapping("sets/names")
    public ResponseEntity<List<SetLookup>> getSetNames(){
        return ResponseEntity.ok(magicService.getBasicSetInfo());
    }

    @GetMapping("cards/types")
    public ResponseEntity<List<String>> getAllCardTypes(){
        return ResponseEntity.ok(magicService.getCardTypes());
    }

    @GetMapping("cards/sortOptions")
    public ResponseEntity<List<String>> getSortOptions(){
        var keyList = new ArrayList<>(magicService.getSortMappingKeys());
        return ResponseEntity.ok(keyList);
    }
}
