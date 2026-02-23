package de.hitohitonika.tcgs.cardcollectionservice.importers.rawdata;

import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.OpCard;

public record OPCardImportData(
        Long inventory_price,
        Long market_price,
        String card_name,
        String set_name,
        String card_text,
        String set_id,
        String rarity,
        String card_set_id,
        String card_color,
        String card_type,
        String life,
        String card_cost,
        String card_power,
        String sub_types,
        Long counter_amount,
        String attribute,
        String date_scrapped,
        String card_image
) {
    public static OpCard toEntity(OPCardImportData opCardImportData) {
        var opCard = new OpCard();

        opCard.setCardCode(opCardImportData.card_set_id);
        opCard.setName(opCardImportData.card_name);
        opCard.setCardText(opCardImportData.card_text);
        opCard.setCardType(opCardImportData.card_type);
        opCard.setRarity(opCardImportData.rarity);
        opCard.setSubTypes(opCardImportData.sub_types);
        opCard.setImage(opCardImportData.card_image);
        opCard.setDateScrapped(opCardImportData.date_scrapped);

        return opCard;
    }
}
