package org.factoriaf5.digital_academy.funko_shop.stripe;

import lombok.*;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private Integer amount; 
    private String currency;
    private List<ProductPurchase> products;
}
