package domain.value;

public record Distance(int meters) {
    public Distance {
        if (meters <= 0) {
            throw new IllegalArgumentException("Distance must be positive");
        }
    }

    public static Distance ofMeters(int meters) {
        return new Distance(meters);
    }
}
