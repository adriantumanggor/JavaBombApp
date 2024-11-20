package repository.impl;

import domain.bomb.Bomb;
import repository.IBombRepository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryBombRepository implements IBombRepository {
    private final Map<String, Bomb> bombs = new ConcurrentHashMap<>();
    private final List<Runnable> listeners = new ArrayList<>();

    public void addListener(Runnable listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (Runnable listener : listeners) {
            listener.run();
        }
    }

    @Override
    public void save(Bomb bomb) {
        bombs.put(bomb.getId(), bomb);
        notifyListeners();
    }

    @Override
    public void delete(String bombId) {
        bombs.remove(bombId);
        notifyListeners();
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