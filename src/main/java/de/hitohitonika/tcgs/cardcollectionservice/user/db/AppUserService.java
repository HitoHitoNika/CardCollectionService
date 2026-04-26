package de.hitohitonika.tcgs.cardcollectionservice.user.db;

import de.hitohitonika.tcgs.cardcollectionservice.data.TcgPrint;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.services.TcgServiceHelper;
import de.hitohitonika.tcgs.cardcollectionservice.user.dto.AppUserDto;
import de.hitohitonika.tcgs.cardcollectionservice.user.dto.AppUserPrintDto;
import de.hitohitonika.tcgs.cardcollectionservice.user.dto.CreatePrintDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
public class AppUserService {
    private final AppUserRepository userRepository;
    private final AppUserPrintRepository userPrintRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TcgServiceHelper tcgServiceHelper;

    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public AppUser findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Transactional
    public void createUser(String username, String password) {
        var user = new AppUser();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }

    @Transactional
    public void addPrintsToUser(UserDetails userDetails, List<CreatePrintDto> createPrintDtos) {
        var user = findByUsername(userDetails.getUsername());

        var userPrints = createPrintDtos.stream()
                .map(print -> {
                    AppUserPrint userPrint = print.toEntity();
                    userPrint.setUser(user);
                    return userPrint;
                })
                .toList();

        userPrintRepository.saveAll(userPrints);
    }

    public AppUserDto mapToDto(UserDetails userDetails) {
        var appUser = findByUsername(userDetails.getUsername());
        return mapToDto(appUser);
    }

    public AppUserDto mapToDto(AppUser appUser) {
        record UserPrintPair(AppUserPrint userPrint, TcgPrint print){}

        var prints = appUser.getCollectedCards().stream()
                .map(userPrint -> {
                    var service = tcgServiceHelper.getService(userPrint.getGameType());
                    var print = service.getPrint(userPrint.getPrintId()).orElse(null);
                    return new UserPrintPair(userPrint, print);
                })
                .filter(pair -> pair.print != null)
                .map(pair -> AppUserPrintDto.fromEntity(pair.userPrint, pair.print))
                .toList();

        return AppUserDto.fromEntity(appUser, prints);
    }
}
