package domain.bomb;

/**
 * Represents the state of a bomb with immutable data.
 */
public class BombState {
    private final String name;
    private final String location;
    private final boolean active;
    private final BombType type;
    
    public BombState(String name, String location, boolean active, BombType type) {
        this.name = name;
        this.location = location;
        this.active = active;
        this.type = type;
    }
    
    public String getName() { return name; }
    public String getLocation() { return location; }
    public boolean isActive() { return active; }
    public BombType getType() { return type; }
}