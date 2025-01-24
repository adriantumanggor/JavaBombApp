package repository.impl;

import domain.bomb.Bomb;
import repository.IBombRepository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryBombRepository implements IBombRepository {
    private final Map<String, Bomb> bombs = new ConcurrentHashMap<>();

    
    public void save(Bomb bomb) {
        bombs.put(bomb.getId(), bomb);
    }

    @Override
    public void delete(String bombId) {
        bombs.remove(bombId);
    }

    @Override
    public Optional<Bomb> findById(String id) {
        return Optional.ofNullable(bombs.get(id));
    }

    @Override
    public List<Bomb> findAll() {
        return new ArrayList<>(bombs.values());
    }
}