package de.hitohitonika.tcgs.cardcollectionservice.data.db.specifications;

import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.YgoCardPrint;
import org.springframework.data.jpa.domain.Specification;

public class YgoCardPrintSpecifications {

    public static Specification<YgoCardPrint> hasCardNameLike(String name) {
        return (root, _, cb) -> {
            if (name == null || name.isBlank()) return null;
            return cb.like(cb.lower(root.get("originalCard").get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<YgoCardPrint> hasCardType(String type) {
        return (root, _, cb) -> {
            if (type == null || type.isBlank()) return null;
            return cb.equal(root.get("originalCard").get("type"), type);
        };
    }

    public static Specification<YgoCardPrint> hasSetId(Long setId) {
        return (root, _, cb) -> {
            if (setId == null) return null;
            return cb.equal(root.get("set").get("id"), setId);
        };
    }
}