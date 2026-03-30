package de.hitohitonika.tcgs.cardcollectionservice.op.data.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Data
public class OpSet {
    @Id
    private String id;
    @OneToMany(mappedBy = "set", cascade = CascadeType.ALL)
    List<OpCard> cards;
    @Column(unique = true)
    private String setCode;
    private String setName;

    public OpSet() {
        id = UUID.randomUUID().toString();
    }
}
