package de.hitohitonika.tcgs.cardcollectionservice.controller;

import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.GameType;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.TCGPrint;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.projections.SetLookup;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.services.TcgServiceHelper;
import de.hitohitonika.tcgs.cardcollectionservice.data.dtos.TcgPrintDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BasicTcgInfoController {

    private final TcgServiceHelper tcgServiceHelper;

    @GetMapping("{tcg}/prints")
    public ResponseEntity<Page<TcgPrintDto>> getCards(
            @PathVariable String tcg,
            @RequestParam(required = false) Long setId,
            @RequestParam(required = false) String type,
            @RequestParam(name = "name-like", required = false) String nameLike,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "setCode") String sortBy,
            @RequestParam(defaultValue = "asc") String order
    ) {
        var service = tcgServiceHelper.getService(GameType.fromString(tcg));

        Page<? extends TCGPrint> printPage =
                service.getPrints(nameLike, type, setId, page, size, sortBy, order);

        Page<TcgPrintDto> dtoPage = printPage.map(TCGPrint::toDto);

        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("{tcg}/sets/names")
    public ResponseEntity<List<SetLookup>> getSetNames(@PathVariable String tcg) {
        var service = tcgServiceHelper.getService(GameType.fromString(tcg));
        return ResponseEntity.ok(
                service.getBasicSetInfo()
        );
    }

    @GetMapping("{tcg}/cards/types")
    public ResponseEntity<List<String>> getAllCardTypes(@PathVariable String tcg) {
        var service = tcgServiceHelper.getService(GameType.fromString(tcg));
        return ResponseEntity.ok(service.getCardTypes());
    }

    @GetMapping("{tcg}/prints/sortOptions")
    public ResponseEntity<List<String>> getSortOptions(@PathVariable String tcg) {
        var service = tcgServiceHelper.getService(GameType.fromString(tcg));
        List<String> sortOptions = new ArrayList<>(service.getSortMappingKeys());
        return ResponseEntity.ok(sortOptions);
    }

}
