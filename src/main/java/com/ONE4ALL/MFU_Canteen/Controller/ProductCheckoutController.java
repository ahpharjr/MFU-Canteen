package com.ONE4ALL.MFU_Canteen.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ONE4ALL.MFU_Canteen.Entity.CheckoutRequest;
import com.ONE4ALL.MFU_Canteen.Entity.ItemQuantity;
import com.ONE4ALL.MFU_Canteen.Entity.StripeResponse;
import com.ONE4ALL.MFU_Canteen.Service.StripeService;

@RestController
@RequestMapping("/api/payment")
public class ProductCheckoutController {

    private final StripeService stripeService;

    public ProductCheckoutController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<StripeResponse> checkoutProducts(@RequestParam Long userId , @RequestBody CheckoutRequest checkoutRequest) {
        System.out.println("Received checkout request: " + checkoutRequest);
        System.out.println("Received checkout request for User ID: " + userId);

        for (ItemQuantity item : checkoutRequest.getItems()) {
            System.out.println("Processing Item ID: " + item.getItemId() + ", Quantity: " + item.getQuantity());
        }

        try {
            StripeResponse stripeResponse = stripeService.checkoutProducts(userId, checkoutRequest);
            return ResponseEntity.status(HttpStatus.OK).body(stripeResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new StripeResponse("FAILED", "Payment processing failed", "", ""));
        }
    }

    
}
