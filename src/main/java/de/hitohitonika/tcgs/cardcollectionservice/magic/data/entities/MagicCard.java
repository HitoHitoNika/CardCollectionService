package de.hitohitonika.tcgs.cardcollectionservice.magic.data.entities;

import de.hitohitonika.tcgs.cardcollectionservice.data.GameType;
import de.hitohitonika.tcgs.cardcollectionservice.data.TcgCard;
import de.hitohitonika.tcgs.cardcollectionservice.data.TcgPrint;
import de.hitohitonika.tcgs.cardcollectionservice.data.dtos.TcgCardDto;
import de.hitohitonika.tcgs.cardcollectionservice.data.dtos.TcgPrintDto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Entity
@Data
public class MagicCard implements TcgCard, TcgPrint {
    @Id
    private String importId;

    private String name;

    private String imageUrl;

    private String manaCost;

    private String type;

    private String rarity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "set_id")
    private MagicSet set;

    @Override
    public TcgCardDto toCardDto() {
        return new TcgCardDto(
                name,
                "",
                "",
                type,
                importId,
                imageUrl,
                List.of(),
                Map.of(
                        "Mana Cost", manaCost
                )
        );
    }

    @Override
    public TcgPrintDto toPrintDto() {
        return new TcgPrintDto(
                name,
                set.getName(),
                imageUrl,
                rarity,
                "",
                importId,
                importId,
                GameType.MAGIC.getKey()
        );
    }
}
