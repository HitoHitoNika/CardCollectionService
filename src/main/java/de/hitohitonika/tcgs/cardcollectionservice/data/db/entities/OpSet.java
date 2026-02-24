package de.hitohitonika.tcgs.cardcollectionservice.data.db.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class OpSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "set", cascade = CascadeType.ALL)
    List<OpCard> cards;
    @Column(unique = true)
    private String setId;
    private String setName;
}
