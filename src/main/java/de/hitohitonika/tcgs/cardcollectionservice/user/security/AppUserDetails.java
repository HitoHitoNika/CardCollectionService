package de.hitohitonika.tcgs.cardcollectionservice.user.security;

import de.hitohitonika.tcgs.cardcollectionservice.user.db.AppUser;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record AppUserDetails(String username, String password) implements UserDetails {
    @Override
    @NullMarked
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var authority = new SimpleGrantedAuthority("ROLE_USER");
        return List.of(authority);
    }

    @Override
    @NullMarked
    public String getPassword() {
        return password;
    }

    @Override
    @NullMarked
    public String getUsername() {
        return username;
    }

    public static AppUserDetails fromEntity(AppUser user) {
        return new AppUserDetails(user.getUsername(), user.getPassword());
    }
}
