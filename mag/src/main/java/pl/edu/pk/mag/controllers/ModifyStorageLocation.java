package pl.edu.pk.mag.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyStorageLocation {

    @Size(max = 50)
    @NotBlank
    private String Code;
    @Valid
    private AddProductToStorageLocation addProduct;
    @Valid
    private MoveProduct moveProduct;
    @Valid
    private AddQuantity addQuantity;

    private boolean removeProduct;

    @Data
    public static class AddProductToStorageLocation {
        @Size(max = 100)
        @NotBlank
        String code;
        @Size(max = 1000)
        String desc;
        @NotNull
        @DecimalMin(value = "0.0")
        @Digits(fraction = 3, integer = 10)
        private BigDecimal quantity = new BigDecimal("0.000");
    }

    @Data
    public static class MoveProduct {
        @Size(max = 50)
        @NotNull
        String destinationShelfCode;
    }

    @Data
    public static class AddQuantity {
        @Digits(fraction = 3, integer = 10)
        @NotNull
        private BigDecimal quantity = new BigDecimal("0.000");
    }

}
