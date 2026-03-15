package de.hitohitonika.tcgs.cardcollectionservice.data.db.entities;

import de.hitohitonika.tcgs.cardcollectionservice.data.dtos.TcgCardDto;

public interface TcgCard {
    TcgCardDto toCardDto();
}
