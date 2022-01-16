package pl.edu.pk.mag.repository.entity.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.edu.pk.mag.repository.dto.BasketItem;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Converter
public class BasketItemConverter implements AttributeConverter<List<BasketItem>, String> {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<BasketItem> basketItems) {
        if (basketItems == null)
            return null;

        try {
            return objectMapper.writeValueAsString(basketItems);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    @Override
    public List<BasketItem> convertToEntityAttribute(String s) {
        if (s == null || s.equals(""))
            return new ArrayList<>();
        try {
            return Arrays.asList(objectMapper.readValue(s, BasketItem[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }
}
