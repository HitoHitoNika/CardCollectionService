package de.hitohitonika.tcgs.cardcollectionservice.magic.data.services;

import de.hitohitonika.tcgs.cardcollectionservice.magic.data.entities.MagicSet;
import de.hitohitonika.tcgs.cardcollectionservice.magic.data.repositories.MagicSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MagicSetService {
    private final MagicSetRepository magicSetRepository;

    public List<MagicSet> getAll() {
        return magicSetRepository.findAll();
    }

    public void save(MagicSet magicSet) {
        magicSetRepository.save(magicSet);
    }
}
