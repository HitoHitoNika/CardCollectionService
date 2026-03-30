package de.hitohitonika.tcgs.cardcollectionservice.user.security;

import de.hitohitonika.tcgs.cardcollectionservice.user.db.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final AppUserRepository userRepository;

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);

        return AppUserDetails.fromEntity(user.orElseThrow(() -> new UsernameNotFoundException(username)));
    }
}
