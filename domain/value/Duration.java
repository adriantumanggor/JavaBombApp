// domain/value/Duration.java
package domain.value;

public record Duration(int seconds) {
    public static Duration ofSeconds(int seconds) {
        return new Duration(seconds);
    }
}