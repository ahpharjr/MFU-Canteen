// package com.ONE4ALL.MFU_Canteen.Service;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.ONE4ALL.MFU_Canteen.Entity.Cart;
// import com.ONE4ALL.MFU_Canteen.Entity.Item;
// import com.ONE4ALL.MFU_Canteen.Entity.User;
// import com.ONE4ALL.MFU_Canteen.Repository.CartRepository;
// import com.ONE4ALL.MFU_Canteen.Repository.ItemRepository;

// @Service
// public class CartService {

//     @Autowired
//     private CartRepository cartRepository;

//     @Autowired
//     private ItemRepository itemRepository;

//     public Cart addItemToCart(Long cartId, Long itemId) {
//         Cart cart = cartRepository.findById(cartId).orElse(new Cart());
//         Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));

//         cart.getItems().add(item);
//         item.setCart(cart); // associate item with cart

//         return cartRepository.save(cart);
//     }
// }

