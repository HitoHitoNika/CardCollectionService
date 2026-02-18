package de.hitohitonika.tcgs.cardcollectionservice.data.general;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("info")
public class ApplicationInfoController {

    private final ApplicationConfiguration config;

    public ApplicationInfoController(ApplicationConfiguration config) {
        this.config = config;
    }

    @GetMapping("availableGames")
    public ResponseEntity<List<GameTypes>> getAvailableGames() {
        return ResponseEntity.ok(config.getEnabled());
    }
}
