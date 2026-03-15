package de.hitohitonika.tcgs.cardcollectionservice.user.security;

import de.hitohitonika.tcgs.cardcollectionservice.user.db.AppUserService;
import de.hitohitonika.tcgs.cardcollectionservice.user.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AppUserService appUserService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        if (appUserService.existsByUsername(registerRequest.username())) {
            return ResponseEntity.badRequest().build();
        }

        appUserService.createUser(registerRequest.username(), registerRequest.password());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
