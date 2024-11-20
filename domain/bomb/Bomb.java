package domain.bomb;

import java.awt.Color;
import java.util.Objects;
import java.util.UUID;

public abstract class Bomb {
    private final String id;
    private String name;
    private Color color;
    private boolean active;
    private final BombType type;

    protected Bomb(String name, Color color, BombType type) {
        this.id = UUID.randomUUID().toString();
        setName(name);
        setColor(color);
        this.type = Objects.requireNonNull(type);
        this.active = false;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Color getColor() { return color; }
    public boolean isActive() { return active; }
    public BombType getType() { return type; }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
    }

    public void setColor(Color color) {
        this.color = Objects.requireNonNull(color, "Color cannot be null");
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public abstract String explode();
}
