package com.ONE4ALL.MFU_Canteen.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ONE4ALL.MFU_Canteen.Entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>{
 
    List<Item> findByShop_ShopId(Long shopId);
}
