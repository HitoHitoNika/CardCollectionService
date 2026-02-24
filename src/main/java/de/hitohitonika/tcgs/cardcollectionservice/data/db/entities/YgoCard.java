package de.hitohitonika.tcgs.cardcollectionservice.data.db.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class YgoCard {
    @Id
    private Long id;

    private String name;

    private String type;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String archetype;

    private String ygodeckproURL;

    private String image;

    @OneToMany(mappedBy = "originalCard", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<YgoCardPrint> prints = new ArrayList<>();

    public void addPrint(YgoCardPrint print) {
        if (this.prints == null) this.prints = new ArrayList<>();
        this.prints.add(print);
        print.setOriginalCard(this);
    }
}
