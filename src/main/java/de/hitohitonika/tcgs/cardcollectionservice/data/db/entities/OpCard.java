package de.hitohitonika.tcgs.cardcollectionservice.data.db.entities;

import de.hitohitonika.tcgs.cardcollectionservice.data.dtos.TcgPrintDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OpCard implements TCGPrint{
    @ManyToOne
    @JoinColumn(name = "set_id")
    OpSet set;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    public TcgPrintDto toDto() {
        return new TcgPrintDto(
                getName(),
                getSet() != null ? getSet().getSetName() : null,
                getImage(),
                getRarity(),
                getCardCode()
        );
    }
}
