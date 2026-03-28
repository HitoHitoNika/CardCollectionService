package de.hitohitonika.tcgs.cardcollectionservice.op.data.specifications;

import de.hitohitonika.tcgs.cardcollectionservice.op.data.entities.OpCard;
import org.springframework.data.jpa.domain.Specification;

public class OpCardSpecifications {
    public static Specification<OpCard> hasNameLike(String name) {
        return (root, _, cb) -> {
            if (name == null || name.isBlank()) return null;
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<OpCard> hasCardType(String type) {
        return (root, _, cb) -> {
            if (type == null || type.isBlank()) return null;
            return cb.equal(root.get("cardType"), type);
        };
    }

    public static Specification<OpCard> hasSetId(String setId) {
        return (root, _, cb) -> {
            if (setId == null) return null;
            return cb.equal(root.get("set").get("id"), setId);
        };
    }
}
