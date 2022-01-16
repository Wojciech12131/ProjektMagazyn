package pl.edu.pk.mag.repository.entity.converters;

import pl.edu.pk.mag.repository.entity.enums.OrderStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;

@Converter
public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {
    @Override
    public String convertToDatabaseColumn(OrderStatus orderStatus) {
        if (orderStatus == null) {
            return null;
        }
        return orderStatus.getStatus();
    }

    @Override
    public OrderStatus convertToEntityAttribute(String s) {
        return Arrays
                .stream(OrderStatus.values())
                .filter(orderStatus -> orderStatus.getStatus().equals(s))
                .findFirst().orElse(OrderStatus.PENDING);
    }
}
