package de.hitohitonika.tcgs.cardcollectionservice.ygo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class YgoSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String setName;

    private String setCode;

    @OneToMany(mappedBy = "set", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<YgoCardPrint> prints;
}
