package domain.history;

import domain.bomb.BombType;
import java.util.UUID;

public record ExplosionRecord(
        String id,
        String details,
        BombType bombType) {
    public ExplosionRecord(String details, BombType bombType) {
        this(UUID.randomUUID().toString(), details, bombType);
    }
}
