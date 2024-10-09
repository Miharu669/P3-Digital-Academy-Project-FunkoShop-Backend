package org.factoriaf5.digital_academy.funko_shop.stripe;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.factoriaf5.digital_academy.funko_shop.product.Product;
import org.factoriaf5.digital_academy.funko_shop.stripe.stripe_exceptions.PaymentFailedException;
import org.factoriaf5.digital_academy.funko_shop.stripe.stripe_exceptions.PaymentRequestInvalidException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("${api-endpoint}/payments")
// @PreAuthorize("hasRole('USER') or hasRole('ADMIN')") // Ensure proper security configuration
public class PaymentController {


    @Value("${STRIPE_SECRET_KEY}")
    private String stripeApiKey;

    
    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@Valid @RequestBody PaymentRequest paymentRequest) {
        Stripe.apiKey = stripeApiKey;


        try {
            Map<String, Object> params = new HashMap<>();
            params.put("payment_method_types", List.of("card"));

            List<Map<String, Object>> lineItems = new java.util.ArrayList<>();
            for (ProductPurchase purchase : paymentRequest.getProducts()) {
                Product product = purchase.getProduct();
                int quantity = purchase.getQuantity();

                if (product.getStock() < quantity) {
                    throw new PaymentRequestInvalidException("Insufficient stock for product: " + product.getName());
                }

                Map<String, Object> item = new HashMap<>();
                item.put("price_data", Map.of(
                        "currency", paymentRequest.getCurrency(),
                        "product_data", Map.of("name", product.getName()),
                        "unit_amount", Math.round(product.getPrice() * 100) 
                ));
                // item.put("quantity", quantity);
                lineItems.add(item);
            }
            params.put("line_items", lineItems);
            params.put("mode", "payment");

            ObjectMapper objectMapper = new ObjectMapper();
            String productsJson = objectMapper.writeValueAsString(paymentRequest.getProducts());

            params.put("metadata", Map.of("products", productsJson));

            Session session = Session.create(params);


            Map<String, String> responseData = new HashMap<>();
            responseData.put("url", session.getUrl());

            return ResponseEntity.ok(responseData);
        } catch (StripeException e) {
            throw new PaymentFailedException("Unable to process the payment. Please try again later.", e);
        } catch (JsonProcessingException e) {
            throw new PaymentFailedException("Error processing product data.", e);
        }
    }

    }

