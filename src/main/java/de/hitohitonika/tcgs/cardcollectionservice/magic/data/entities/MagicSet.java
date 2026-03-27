package de.hitohitonika.tcgs.cardcollectionservice.magic.data.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class MagicSet {
    @Id
    private String importId;

    private String name;

    @OneToMany(mappedBy = "set", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MagicCard> cards;
}
