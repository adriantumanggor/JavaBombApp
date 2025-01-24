package domain.value;

public record Distance(int meters) {
    public static Distance ofMeters(int meters) {
        return new Distance(meters);
    }
}
