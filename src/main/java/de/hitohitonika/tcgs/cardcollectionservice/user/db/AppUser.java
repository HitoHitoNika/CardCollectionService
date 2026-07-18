package de.hitohitonika.tcgs.cardcollectionservice.user.db;

import de.hitohitonika.tcgs.cardcollectionservice.data.GameType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class AppUser {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private List<AppUserPrint> collectedCards;

    @ElementCollection(targetClass = GameType.class)
    @CollectionTable(name = "user_games", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "game_type")
    private List<GameType> games = new ArrayList<>();
}
