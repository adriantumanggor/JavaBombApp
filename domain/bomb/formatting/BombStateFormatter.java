package domain.bomb.formatting;

import domain.bomb.BombState;

/**
 * Utility class for formatting bomb state information.
 */
public final class BombStateFormatter {
    
    private BombStateFormatter() {
    }
    
    public static String formatCurrentState(BombState state) {
        return String.format("Bomb Name: %s, Location: %s, Type: %s, Activated: %s",
                           state.getName(),
                           state.getLocation(),
                           state.getType(),
                           state.isActive() ? "Yes" : "No");
    }
}