package com.ONE4ALL.MFU_Canteen.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ONE4ALL.MFU_Canteen.Entity.CartItem;
import com.ONE4ALL.MFU_Canteen.Entity.CheckoutRequest;
import com.ONE4ALL.MFU_Canteen.Entity.Item;
import com.ONE4ALL.MFU_Canteen.Entity.ItemQuantity;
import com.ONE4ALL.MFU_Canteen.Entity.StripeResponse;
import com.ONE4ALL.MFU_Canteen.Repository.CartItemRepository;
import com.ONE4ALL.MFU_Canteen.Repository.ItemRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;


@Service
public class StripeService {

    @Value("${stripe.secretKey}")
    private String secretKey;

    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;

    @Autowired
    public StripeService(ItemRepository itemRepository, CartItemRepository cartItemRepository) {
        this.itemRepository = itemRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public StripeResponse checkoutProducts(Long userId, CheckoutRequest checkoutRequest) {
        Stripe.apiKey = secretKey;
    
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
        List<Long> selectedCartItemIds = new ArrayList<>();
    
        for (ItemQuantity itemQuantity : checkoutRequest.getItems()) {

            CartItem cartItem = cartItemRepository.findByUserIdAndItemId(userId, itemQuantity.getItemId())
                    .orElseThrow(() -> new RuntimeException("CartItem not found for Item ID: " + itemQuantity.getItemId()));
    
            selectedCartItemIds.add(cartItem.getId()); 
    
            SessionCreateParams.LineItem.PriceData.ProductData productData =
                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName(cartItem.getItem().getName())
                            .build();
    
            SessionCreateParams.LineItem.PriceData priceData =
                    SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("THB")
                            .setUnitAmount((long) (cartItem.getItem().getPrice() * 100))
                            .setProductData(productData)
                            .build();
    
            SessionCreateParams.LineItem lineItem =
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(itemQuantity.getQuantity())
                            .setPriceData(priceData)
                            .build();
    
            lineItems.add(lineItem);
        }
    
        String successUrl = "http://localhost:8080/api/payment/success?userId=" + userId +
                            "&selectedCartItemIds=" + selectedCartItemIds.stream()
                            .map(String::valueOf).collect(Collectors.joining(","));
    
        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(successUrl)
                    .setCancelUrl("http://localhost:8080/payment-cancel")
                    .addAllLineItem(lineItems)
                    .build();
    
            Session session = Session.create(params);
    
            return new StripeResponse("SUCCESS", "Payment session created", session.getId(), session.getUrl());
        } catch (StripeException e) {
            e.printStackTrace();
            return new StripeResponse("FAILED", "Stripe error: " + e.getMessage(), "", "");
        }
    }
    

}
