package domain.bomb.impl;

import domain.bomb.Bomb;
import domain.bomb.BombType;
import domain.value.Duration;
import java.util.Objects;

public class TimedBomb extends Bomb {
    private Duration duration;

    public TimedBomb(String name, String location, Duration duration) {
        super(name, location, BombType.TIMED);
        setDuration(duration);
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = Objects.requireNonNull(duration, "Duration cannot be null");
    }

    @Override
    public String explode() {
        return String.format("%s exploded after %d seconds!", getName(), duration.seconds());
    }
}