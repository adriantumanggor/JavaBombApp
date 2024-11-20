// domain/bomb/RemoteBomb.java
package domain.bomb;

import java.awt.Color;

public class RemoteBomb extends Bomb {
    private String frequency;

    public RemoteBomb(String name, Color color, String frequency) {
        super(name, color, BombType.REMOTE);
        setFrequency(frequency);
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        if (frequency == null || frequency.trim().isEmpty()) {
            throw new IllegalArgumentException("Frequency cannot be null or empty");
        }
        this.frequency = frequency;
    }

    @Override
    public String explode() {
        return String.format("%s detonated by remote signal on frequency %s!", getName(), frequency);
    }
}