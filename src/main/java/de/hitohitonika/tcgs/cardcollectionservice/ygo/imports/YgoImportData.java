package de.hitohitonika.tcgs.cardcollectionservice.ygo.imports;

import de.hitohitonika.tcgs.cardcollectionservice.ygo.data.entities.YgoCard;
import de.hitohitonika.tcgs.cardcollectionservice.ygo.data.entities.YgoCardPrint;
import de.hitohitonika.tcgs.cardcollectionservice.ygo.data.entities.YgoSet;

import java.util.List;
import java.util.Optional;

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
            card.setAtk(Optional.ofNullable(atk).orElse(0));
            card.setDef(Optional.ofNullable(def).orElse(0));
            card.setLevel(Optional.ofNullable(level).orElse(0));
            card.setRace(race);
            card.setAttribute(attribute);

            if (card_images != null && !card_images.isEmpty()) {
                card.setImage(card_images.getFirst().image_url_small());
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
