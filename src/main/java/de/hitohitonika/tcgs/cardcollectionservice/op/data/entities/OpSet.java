package de.hitohitonika.tcgs.cardcollectionservice.op.data.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class OpSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "set", cascade = CascadeType.ALL)
    List<OpCard> cards;
    @Column(unique = true)
    private String setCode;
    private String setName;
}
