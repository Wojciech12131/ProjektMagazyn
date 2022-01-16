package pl.edu.pk.mag.repository.entity.converters;

import pl.edu.pk.mag.repository.entity.enums.OrderStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {
    @Override
    public String convertToDatabaseColumn(OrderStatus orderStatus) {
        if (orderStatus == null) {
            return null;
        }
        return null;
    }

    @Override
    public OrderStatus convertToEntityAttribute(String s) {
        return null;
    }
}
