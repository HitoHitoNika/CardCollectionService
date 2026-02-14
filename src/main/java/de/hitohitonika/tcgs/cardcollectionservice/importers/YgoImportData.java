package de.hitohitonika.tcgs.cardcollectionservice.importers;

import de.hitohitonika.tcgs.cardcollectionservice.ygo.entities.YgoCard;
import de.hitohitonika.tcgs.cardcollectionservice.ygo.entities.YgoCardPrint;
import de.hitohitonika.tcgs.cardcollectionservice.ygo.entities.YgoSet;

import java.util.List;

public record YgoImportData(List<RawYgoCard> data) {
    public record RawYgoCard(
            long id,
            String name,
            String type,
            String frameType,
            String humanReadableCardType,
            String archetype,
            String desc,
            Integer atk,
            Integer def,
            Integer level,
            String race,
            String attribute,
            String ygoprodeck_url,
            List<RawYgoSet> card_sets,
            List<RawYgoImages> card_images,
            List<RawYgoPrices> card_prices
    ){
        //Erstellt eine YgoCard OHNE die Abhängigkeit zu anderen Entitäten
        public YgoCard basicYgoCard() {
            var card = new YgoCard();

            card.setId(id);
            card.setName(name);
            card.setType(type);
            card.setDescription(desc);
            card.setArchetype(archetype);
            card.setYgodeckproURL(ygoprodeck_url);

            if (card_images != null && !card_images.isEmpty()) {
                card.setImage(card_images.getFirst().image_url());
            }

            return card;
        }
    }

    public record RawYgoSet(
            String set_name,
            String set_code,
            String set_rarity,
            String set_rarity_code,
            String set_price
    ){
        public YgoSet basicYgoSet(String code) {
            var set = new YgoSet();
            set.setSetCode(code);
            set.setSetName(set_name);

            return set;
        }

        public YgoCardPrint basicYgoCardPrint() {
            var print = new YgoCardPrint();

            if (this.set_code != null && this.set_code.contains("-")) {
                var parts = this.set_code.split("-");
                print.setCardNumber(parts.length > 1 ? parts[1] : "UNKNOWN");
            } else {
                print.setCardNumber(this.set_code);
            }

            print.setRarity(set_rarity);

            return print;
        }
    }

    public record RawYgoImages(
            long id,
            String image_url,
            String image_url_small,
            String image_url_cropped
    ){}

    public record RawYgoPrices(
            String cardmarket_price,
            String tcgplayer_price,
            String ebay_price,
            String amazon_price,
            String coolstuffinc_price
    ){}
}
