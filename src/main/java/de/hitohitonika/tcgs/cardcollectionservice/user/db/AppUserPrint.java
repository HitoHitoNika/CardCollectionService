package de.hitohitonika.tcgs.cardcollectionservice.user.db;

import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.GameType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class AppUserPrint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;

    @Enumerated(EnumType.STRING)
    private GameType gameType;

    private long printId;

    @Enumerated(EnumType.STRING)
    private CardCondition condition;

    private String description;

    private int amount;
}
