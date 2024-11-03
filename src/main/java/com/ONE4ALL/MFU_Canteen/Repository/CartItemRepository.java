package com.ONE4ALL.MFU_Canteen.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ONE4ALL.MFU_Canteen.Entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{
    
}
