package de.hitohitonika.tcgs.cardcollectionservice.op.data.entities;

import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.GameType;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.TcgCard;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.TcgPrint;
import de.hitohitonika.tcgs.cardcollectionservice.data.dtos.TcgCardDto;
import de.hitohitonika.tcgs.cardcollectionservice.data.dtos.TcgPrintDto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class OpCard implements TcgPrint, TcgCard {
    @ManyToOne
    @JoinColumn(name = "set_id")
    OpSet set;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String cardCode;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String cardText;
    private String cardType;
    private String rarity;
    private String subTypes;
    private String image;

    private String dateScrapped;

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
