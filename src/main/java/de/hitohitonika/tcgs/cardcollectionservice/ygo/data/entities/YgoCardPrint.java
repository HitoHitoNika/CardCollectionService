package de.hitohitonika.tcgs.cardcollectionservice.ygo.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.GameType;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.TcgPrint;
import de.hitohitonika.tcgs.cardcollectionservice.data.dtos.TcgPrintDto;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class YgoCardPrint implements TcgPrint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "set_id")
    private YgoSet set;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    @JsonIgnore
    private YgoCard originalCard;

    private String cardNumber;

    private String rarity;

    @Override
    public TcgPrintDto toPrintDto() {
        return new TcgPrintDto(
                getOriginalCard().getName(),
                getSet().getSetName(),
                getOriginalCard().getImage(),
                getRarity(),
                getSet().getSetCode() + '-' + getCardNumber(),
                getOriginalCard().getId(),
                getId(),
                GameType.YGO.getKey()
        );
    }
}
