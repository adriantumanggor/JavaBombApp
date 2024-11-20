// domain/value/Duration.java
package domain.value;

public record Duration(long seconds) {
    public Duration {
        if (seconds <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }
    }

    public static Duration ofSeconds(long seconds) {
        return new Duration(seconds);
    }
}