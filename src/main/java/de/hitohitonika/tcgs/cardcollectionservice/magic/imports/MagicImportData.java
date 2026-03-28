package de.hitohitonika.tcgs.cardcollectionservice.magic.imports;

import de.hitohitonika.tcgs.cardcollectionservice.magic.data.entities.MagicCard;
import de.hitohitonika.tcgs.cardcollectionservice.magic.data.entities.MagicSet;

import java.util.Map;

public record MagicImportData(
        String object,
        String id,
        String name,
        Map<String,String> image_uris,
        String mana_cost,
        String type_line,
        String set_id,
        String set_name,
        String rarity
) {
    public MagicCard basicCardEntity(){
        var entity = new MagicCard();
        entity.setImportId(id);
        entity.setName(name);
        entity.setRarity(rarity);
        entity.setManaCost(mana_cost);
        entity.setType(type_line);

        if(image_uris != null) {
            var imageUrl = image_uris.containsKey("normal") ?
                    image_uris.get("normal") :
                    image_uris.get("small");
            entity.setImageUrl(imageUrl);
        } else {
            entity.setImageUrl(null);
        }


        return entity;
    }

    public MagicSet basicSetEntity(){
        var entity = new MagicSet();
        entity.setImportId(set_id);
        entity.setName(name);
        return entity;
    }
}
