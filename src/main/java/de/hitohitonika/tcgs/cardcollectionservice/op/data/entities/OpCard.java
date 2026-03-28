package de.hitohitonika.tcgs.cardcollectionservice.op.data.entities;

import de.hitohitonika.tcgs.cardcollectionservice.data.GameType;
import de.hitohitonika.tcgs.cardcollectionservice.data.TcgCard;
import de.hitohitonika.tcgs.cardcollectionservice.data.TcgPrint;
import de.hitohitonika.tcgs.cardcollectionservice.data.dtos.TcgCardDto;
import de.hitohitonika.tcgs.cardcollectionservice.data.dtos.TcgPrintDto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Data
public class OpCard implements TcgPrint, TcgCard {
    @Id
    private String id;
    private String cardCode;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String cardText;
    private String cardType;
    private String rarity;
    private String subTypes;
    private String image;

    private String dateScrapped;

    @ManyToOne
    @JoinColumn(name = "set_id")
    OpSet set;

    public OpCard() {
        id = UUID.randomUUID().toString();
    }

    @Override
    public TcgPrintDto toPrintDto() {
        return new TcgPrintDto(
                getName(),
                getSet() != null ? getSet().getSetName() : null,
                getImage(),
                getRarity(),
                getCardCode(),
                getId(),
                getId(),
                GameType.OP.getKey()
        );
    }

    @Override
    public TcgCardDto toCardDto() {
        //TODO: Tatsächliche Prints suchbar machen...
        return new TcgCardDto(
                name,cardText,subTypes,cardType,id,image, List.of(toPrintDto()),null
        );
    }
}
