package com.ONE4ALL.MFU_Canteen.Entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cartId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    public Cart() {
        this.cartItems = new ArrayList<>(); // Explicit initialization
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    /**
     * Dynamically calculate total quantity of items in the cart.
     * @return total quantity
     */
    public int getTotalQuantity() {
        return cartItems.stream()
            .mapToInt(CartItem::getQuantity)
            .sum();
    }

    /**
     * Dynamically calculate the total price of items in the cart.
     * @return total price
     */
    public double getTotalPrice() {
        return cartItems.stream()
            .mapToDouble(cartItem -> cartItem.getItem().getPrice() * cartItem.getQuantity())
            .sum();
    }
    
}
