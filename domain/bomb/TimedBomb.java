package domain.bomb;

import domain.value.Duration;
import java.awt.Color;
import java.util.Objects;

public class TimedBomb extends Bomb {
    private Duration duration;

    public TimedBomb(String name, Color color, Duration duration) {
        super(name, color, BombType.TIMED);
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