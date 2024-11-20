// domain/value/Duration.java
package domain.value;

public record Duration(int seconds) {
    public Duration {
        if (seconds <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }
    }

    public static Duration ofSeconds(int seconds) {
        return new Duration(seconds);
    }
}