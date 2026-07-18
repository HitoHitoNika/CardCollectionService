package de.hitohitonika.tcgs.cardcollectionservice.user.security;

import de.hitohitonika.tcgs.cardcollectionservice.user.db.AppUserService;
import de.hitohitonika.tcgs.cardcollectionservice.user.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AppUserService appUserService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        log.info("Register request: {}", registerRequest);
        if (appUserService.existsByUsername(registerRequest.username())) {
            log.info("Username already exists!");
            return ResponseEntity.badRequest().build();
        }


        appUserService.createUser(registerRequest.username(), registerRequest.password(), registerRequest.games());
        log.info("User created!");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
