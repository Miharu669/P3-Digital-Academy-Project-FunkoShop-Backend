package org.factoriaf5.digital_academy.funko_shop.stripe;

import org.factoriaf5.digital_academy.funko_shop.product.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPurchase {
    @NotNull(message = "Product is required.")
    private Product product;

    @Min(value = 1, message = "Quantity must be at least 1.")
    private int quantity;

}
