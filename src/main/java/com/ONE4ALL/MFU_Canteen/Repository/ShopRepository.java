package com.ONE4ALL.MFU_Canteen.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ONE4ALL.MFU_Canteen.Entity.Canteen;
import com.ONE4ALL.MFU_Canteen.Entity.Shop;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    // Fetch shops based on the Canteen ID
    List<Shop> findByCanteen(Canteen canteen);
}


