package com.ONE4ALL.MFU_Canteen.Entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Cart {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cartId;
    private Integer totalQuantity;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // @ManyToMany(cascade = CascadeType.ALL)
    // @JoinTable(
    //     name = "cart_item",
    //     joinColumns = @JoinColumn(name = "cart_id"),
    //     inverseJoinColumns = @JoinColumn(name = "item_id")
    // )
    // private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    public Cart(){

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

    // public Integer getTotalQuantity() {
    //     return totalQuantity;
    // }

    public int getTotalQuantity() {
        return cartItems.stream()
                        .mapToInt(CartItem::getQuantity)
                        .sum();
    }
    
    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    
}
