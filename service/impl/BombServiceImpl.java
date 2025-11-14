package service.impl;

import domain.bomb.Bomb;
import domain.history.ExplosionRecord;
import repository.IBombRepository;
import repository.IHistoryRepository;

import service.IBombService;

import java.util.List;
import java.util.Objects;

public class BombServiceImpl implements IBombService {
    private final IBombRepository bombRepository;
    private final IHistoryRepository historyRepository;

    public BombServiceImpl(IBombRepository bombRepository, IHistoryRepository historyRepository) {
        this.bombRepository = bombRepository;
        this.historyRepository = historyRepository;
    }

    @Override
    public void addBomb(Bomb bomb) {
        Objects.requireNonNull(bomb, "Bomb cannot be null");
        bombRepository.save(bomb);
    }
    
    @Override
    public void removeBomb(String bombId) {
        Bomb bomb = findBombById(bombId);
        bombRepository.delete(bomb.getId());
    }
    
    @Override
    public void activateBomb(String bombId) {
        Bomb bomb = findBombById(bombId);
        bomb.activate();
        bombRepository.save(bomb);
    }

    @Override
    public void deactivateBomb(String bombId) {
        Bomb bomb = findBombById(bombId);
        bomb.deactivate();
        bombRepository.save(bomb); // Save the updated state
        String explosionDetails = bomb.getCurrentState();
        ExplosionRecord record = new ExplosionRecord(explosionDetails, bomb.getType());

        historyRepository.saveRecord(record);

    }

    @Override
    public void explodeBomb(String bombId) {
        Bomb bomb = findBombById(bombId);
        if (!bomb.isActive()) {
            throw new IllegalStateException("Cannot explode inactive bomb");
        }

        String explosionDetails = bomb.explode();
        ExplosionRecord record = new ExplosionRecord(explosionDetails, bomb.getType());

        historyRepository.saveRecord(record);

        bomb.deactivate();
        bombRepository.save(bomb);
    }

    @Override
    public List<Bomb> getAllBombs() {
        return bombRepository.findAll();
    }

    @Override
    public List<ExplosionRecord> getExplosionHistory() {
        return historyRepository.getAllRecords();
    }

    private Bomb findBombById(String bombId) {
        Objects.requireNonNull(bombId, "Bomb ID cannot be null");
        return bombRepository.findById(bombId)
                .orElseThrow(() -> new IllegalArgumentException("No bomb found with ID: " + bombId));
    }

}