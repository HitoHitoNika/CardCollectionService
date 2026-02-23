package de.hitohitonika.tcgs.cardcollectionservice.data.db.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OpCard {
    @ManyToOne
    @JoinColumn(name = "set_id")
    OpSet set;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cardCode;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String cardText;
    private String cardType;
    private String rarity;
    private String subTypes;
    private String image;

    private String dateScrapped;
}
