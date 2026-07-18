package de.hitohitonika.tcgs.cardcollectionservice.user;

import de.hitohitonika.tcgs.cardcollectionservice.user.db.AppUser;
import de.hitohitonika.tcgs.cardcollectionservice.user.dto.AppUserDto;
import de.hitohitonika.tcgs.cardcollectionservice.user.db.AppUserService;
import de.hitohitonika.tcgs.cardcollectionservice.user.dto.CreatePrintDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@Slf4j
public class AppUserController {
    private final AppUserService appUserService;

    @GetMapping("me")
    public ResponseEntity<AppUserDto> me(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("Me for User {}", userDetails.getUsername());
        var userDto = appUserService.mapToDto(userDetails);

        return ResponseEntity.ok(userDto);
    }

    @PostMapping("prints/addPrints")
    public ResponseEntity<Void> addPrints(@AuthenticationPrincipal UserDetails userDetails, @RequestBody List<CreatePrintDto> createPrintDtos) {
        appUserService.addPrintsToUser(userDetails,createPrintDtos);

        return ResponseEntity.created(URI.create("/api/user/prints")).build();
    }

    @GetMapping("{username}")
    public ResponseEntity<AppUserDto> getUser(@PathVariable String username) {
        AppUser user = appUserService.findByUsername(username);

        var dto = appUserService.mapToDto(user);

        return ResponseEntity.ok(dto);
    }


}
