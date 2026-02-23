package de.hitohitonika.tcgs.cardcollectionservice.data.db.specifications;

import de.hitohitonika.tcgs.cardcollectionservice.data.db.entities.YgoCard;
import org.springframework.data.jpa.domain.Specification;

public class YgoCardSpecifications {
    private YgoCardSpecifications() {}

    public static Specification<YgoCard> hasNameLike(String name) {
        return (root, _, cb) -> name == null ? null :
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<YgoCard> hasType(String type) {
        return (root, _, cb) -> type == null ? null :
                cb.equal(root.get("type"), type);
    }

    public static Specification<YgoCard> isInSet(Long setId) {
        return (root, query, cb) -> {
            if (setId == null) return null;
            query.distinct(true);
            return cb.equal(root.join("prints").get("set").get("id"), setId);
        };
    }
}
