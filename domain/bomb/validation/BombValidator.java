package domain.bomb.validation;

import java.util.Objects;

import domain.bomb.BombType;

public final class BombValidator {
    
    private static final String NAME_NULL_MESSAGE = "Name cannot be null";
    private static final String NAME_EMPTY_MESSAGE = "Name cannot be empty";
    private static final String LOCATION_NULL_MESSAGE = "Location cannot be null";
    
    private BombValidator() {
    }
    
    public static void validateName(String name) {
        Objects.requireNonNull(name, NAME_NULL_MESSAGE);
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException(NAME_EMPTY_MESSAGE);
        }
    }
    
    public static void validateLocation(String location) {
        Objects.requireNonNull(location, LOCATION_NULL_MESSAGE);
    }
    
    public static void validateType(BombType type) {
        Objects.requireNonNull(type, "Type cannot be null");
    }
}