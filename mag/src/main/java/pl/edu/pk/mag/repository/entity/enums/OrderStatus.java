package pl.edu.pk.mag.repository.entity.enums;

public enum OrderStatus {

    PENDING("P"),
    APPROVED("A"),
    CANCELED("C"),
    DONE("D");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
