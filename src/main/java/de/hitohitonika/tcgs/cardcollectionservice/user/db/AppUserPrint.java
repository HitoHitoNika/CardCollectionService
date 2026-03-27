package de.hitohitonika.tcgs.cardcollectionservice.user.db;

import de.hitohitonika.tcgs.cardcollectionservice.data.GameType;
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
    @Column(nullable = false)
    private GameType gameType;

    @Column(nullable = false)
    private long printId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardCondition condition;

    private String description;

    @Column(nullable = false)
    private int amount;
}
