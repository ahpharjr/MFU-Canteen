// package com.ONE4ALL.MFU_Canteen.Entity;

// import java.util.List;

// import jakarta.persistence.CascadeType;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.OneToMany;
// import jakarta.persistence.OneToOne;

// @Entity
// public class Cart {
    
//     @Id
//     @GeneratedValue(strategy = GenerationType.AUTO)
//     private Long cartId;
//     private Integer quantity;

//     // @OneToOne
//     // @JoinColumn(name = "user_id", referencedColumnName = "userId")
//     // private User user;

//     @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
//     private List<Item> items;

//     public Cart(){

//     }

//     public Long getCartId() {
//         return cartId;
//     }

//     public void setCartId(Long cartId) {
//         this.cartId = cartId;
//     }

//     public Integer getQuantity() {
//         return quantity;
//     }

//     public void setQuantity(Integer quantity) {
//         this.quantity = quantity;
//     }

//     // public User getUser() {
//     //     return user;
//     // }

//     // public void setUser(User user) {
//     //     this.user = user;
//     // }

//     public List<Item> getItems() {
//         return items;
//     }

//     public void setItems(List<Item> items) {
//         this.items = items;
//     }

    
// }
