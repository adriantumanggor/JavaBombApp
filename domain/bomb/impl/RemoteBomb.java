package domain.bomb.impl;

import domain.bomb.Bomb;
import domain.bomb.BombType;

public class RemoteBomb extends Bomb {
    private String frequency;

    public RemoteBomb(String name, String location, String frequency) {
        super(name, location, BombType.REMOTE);
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