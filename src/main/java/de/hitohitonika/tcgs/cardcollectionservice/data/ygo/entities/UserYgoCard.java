package de.hitohitonika.tcgs.cardcollectionservice.data.ygo.entities;

import de.hitohitonika.tcgs.cardcollectionservice.data.CardCondition;
import de.hitohitonika.tcgs.cardcollectionservice.user.AppUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserYgoCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int amount;

    private CardCondition condition;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "card_print_id")
    private YgoCardPrint cardPrint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser owner;
}
