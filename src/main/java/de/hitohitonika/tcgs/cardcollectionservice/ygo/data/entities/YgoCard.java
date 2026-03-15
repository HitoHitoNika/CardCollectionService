package de.hitohitonika.tcgs.cardcollectionservice.ygo.data.entities;

import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.TcgCard;
import de.hitohitonika.tcgs.cardcollectionservice.data.dtos.TcgCardDto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
@Data
public class YgoCard implements TcgCard {
    @Id
    private Long id;

    private String name;

    private String type;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String archetype;

    private String ygodeckproURL;

    private String image;

    private int atk;

    private int def;

    private int level;

    private String attribute;

    private String race;


    @OneToMany(mappedBy = "originalCard", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<YgoCardPrint> prints = new ArrayList<>();

    public void addPrint(YgoCardPrint print) {
        if (this.prints == null) this.prints = new ArrayList<>();
        this.prints.add(print);
        print.setOriginalCard(this);
    }

    @Override
    public TcgCardDto toCardDto() {

        var printDtos = prints.stream()
                .map(YgoCardPrint::toPrintDto)
                .toList();

        var customData = new HashMap<String, String>();
        customData.put("Attack",String.valueOf(atk));
        customData.put("Defense",String.valueOf(def));
        customData.put("Level",String.valueOf(level));
        customData.put("Attribute",attribute);
        customData.put("Race",race);

        return new TcgCardDto(
            name,description,archetype,type,id,image,printDtos,customData
        );
    }
}
