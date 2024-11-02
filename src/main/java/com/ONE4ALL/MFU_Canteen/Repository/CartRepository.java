package com.ONE4ALL.MFU_Canteen.Repository;

// import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ONE4ALL.MFU_Canteen.Entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    // Optional<Cart> findByUserId(String userId);
}
