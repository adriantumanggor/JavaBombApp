package domain.bomb;

public enum BombType {
    TIMED("Timed Bomb"),
    SMOKE("Smoke Bomb"),
    REMOTE("Remote Bomb");

    private final String displayName;

    BombType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}