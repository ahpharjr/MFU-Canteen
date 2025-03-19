package com.ONE4ALL.MFU_Canteen.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ONE4ALL.MFU_Canteen.Entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{
    
     @Query("SELECT c FROM CartItem c WHERE c.cart.user.id = :userId AND c.item.id = :itemId")
    Optional<CartItem> findByUserIdAndItemId(@Param("userId") Long userId, @Param("itemId") Long itemId);
}
