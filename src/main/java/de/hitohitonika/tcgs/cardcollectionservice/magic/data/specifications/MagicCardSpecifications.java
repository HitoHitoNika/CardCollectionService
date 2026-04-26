package de.hitohitonika.tcgs.cardcollectionservice.magic.data.specifications;

import de.hitohitonika.tcgs.cardcollectionservice.magic.data.entities.MagicCard;
import org.springframework.data.jpa.domain.Specification;

public class MagicCardSpecifications {
    public static Specification<MagicCard> hasNameLike(String name) {
        return (root, _, cb) -> {
            if(name == null || name.isBlank()) return null;
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<MagicCard> hasCardType(String type) {
        return (root, _, cb) -> {
            if(type == null || type.isBlank()) return null;
            return cb.equal(root.get("type"), type);
        };
    }

    public static Specification<MagicCard> hasSetId(String setId) {
        return (root, _, cb) -> {
            if(setId == null || setId.isBlank()) return null;
            return cb.equal(root.get("set").get("id"), setId);
        };
    }
}
