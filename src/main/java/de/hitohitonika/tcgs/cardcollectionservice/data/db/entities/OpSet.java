package de.hitohitonika.tcgs.cardcollectionservice.data.db.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class OpSet {
    @OneToMany(mappedBy = "set", cascade = CascadeType.ALL)
    List<OpCard> cards;
    @Id
    private String setId;
    private String setName;
}
