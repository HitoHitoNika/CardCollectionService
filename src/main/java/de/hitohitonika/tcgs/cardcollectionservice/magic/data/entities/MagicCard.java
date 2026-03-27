package de.hitohitonika.tcgs.cardcollectionservice.magic.data.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class MagicCard {
    @Id
    private UUID importId;

    private String name;

    private String imageUrl;

    private String manaCost;

    private String type;

    private String rarity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "set_id")
    private MagicSet set;

}
