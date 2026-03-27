package de.hitohitonika.tcgs.cardcollectionservice.op.data;

import de.hitohitonika.tcgs.cardcollectionservice.data.GameType;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.projections.SetLookup;
import de.hitohitonika.tcgs.cardcollectionservice.op.data.entities.OpCard;
import de.hitohitonika.tcgs.cardcollectionservice.op.data.entities.OpSet;
import de.hitohitonika.tcgs.cardcollectionservice.op.data.repositories.OpCardRepository;
import de.hitohitonika.tcgs.cardcollectionservice.op.data.repositories.OpSetRepository;
import de.hitohitonika.tcgs.cardcollectionservice.data.db.services.TcgService;
import de.hitohitonika.tcgs.cardcollectionservice.op.data.specifications.OpCardSpecifications;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OpService implements TcgService<OpCard,OpCard> {
    private final OpSetRepository opSetRepository;

    private final OpCardRepository opCardRepository;

    public static final Map<String, String> SORT_MAPPING = Map.of(
            "name", "name",
            "setName", "set.setName",
            "rarity", "rarity",
            "cardCode", "cardCode"
    );

    public boolean doEntriesExist() {
        return opCardRepository.count() > 0;
    }

    @Override
    public GameType getGameType() {
        return GameType.OP;
    }

    @Override
    public Optional<OpCard> getPrint(long id){
        return opCardRepository.findById(id);
    }

    @Override
    public Optional<OpCard> getCard(long id){
        return opCardRepository.findById(id);
    }

    @Override
    public Page<OpCard> getPrints(String name, String type, Long setId, int page, int size, String sortBy, String sortDirection) {
        String jpaField = SORT_MAPPING.getOrDefault(sortBy, "cardCode");
        Sort.Direction dir = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(dir, jpaField).and(Sort.by(Sort.Direction.ASC, "cardCode")));

        Specification<OpCard> spec = (root, query, cb) -> {
            if (query.getResultType() != Long.class) {
                root.fetch("set", JoinType.LEFT);
            }

            return Specification.where(OpCardSpecifications.hasNameLike(name))
                    .and(OpCardSpecifications.hasCardType(type))
                    .and(OpCardSpecifications.hasSetId(setId))
                    .toPredicate(root, query, cb);
        };

        return opCardRepository.findAll(spec, pageable);
    }

    @Transactional
    public Map<String, Integer> importEverything(List<OpSet> sets, List<OpCard> cards) {
        opSetRepository.saveAll(sets.stream().distinct().toList());

        Set<String> seenInThisRun = new HashSet<>(opCardRepository.findAllCardCompositeKeys());

        List<OpCard> newCards = new ArrayList<>();

        for (OpCard c : cards) {
            String key = (c.getCardCode() + "_" + c.getName() + "_" + c.getRarity()).toLowerCase();

            if (!seenInThisRun.contains(key)) {
                newCards.add(c);
                seenInThisRun.add(key);
            }
        }

        if (!newCards.isEmpty()) {
            opCardRepository.saveAll(newCards);
        }

        return Map.of("newCards", newCards.size(), "newSets", sets.size());
    }

    @Override
    public List<String> getSortMappingKeys() {
        return new ArrayList<>(SORT_MAPPING.keySet());
    }

    @Override
    public List<String> getCardTypes(){
        return opCardRepository.findAllDistinctTypes();
    }

    @Override
    public List<SetLookup> getBasicSetInfo() {
        return opSetRepository.findAllSetNamesAndIds();
    }
}
