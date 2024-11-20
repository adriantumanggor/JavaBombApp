package service;

import domain.bomb.Bomb;
import domain.history.ExplosionRecord;
import java.util.List;

public interface IBombService {
    void addBomb(Bomb bomb);
    void removeBomb(String bombId);
    void activateBomb(String bombId);
    void deactivateBomb(String bombId);
    void explodeBomb(String bombId);
    List<Bomb> getAllBombs();
    List<ExplosionRecord> getExplosionHistory();
}