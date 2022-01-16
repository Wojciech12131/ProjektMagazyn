package pl.edu.pk.mag.requests;


import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class OrderRequest {

    @NotBlank
    private String warehouseCode;

    @Valid
    @NotNull
    private List<BasketItemRequest> basketItem;
}
