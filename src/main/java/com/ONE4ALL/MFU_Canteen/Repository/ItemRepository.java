package com.ONE4ALL.MFU_Canteen.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ONE4ALL.MFU_Canteen.Entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>{
 
    List<Item> findByShop_ShopId(Long shopId);

    @Query("SELECT i FROM Item i WHERE i.shop.canteen.canteenId = :canteenId")
    List<Item> findItemsByCanteenId(@Param("canteenId") Long canteenId);

    List<Item> findByCategoryAndItemIdNot(String category, Long excludeItemId);

    @Query("SELECT i FROM Item i WHERE i.shop.canteen.canteenId = :canteenId AND i.availability = true")
    List<Item> findAvailableItemsByCanteenId(@Param("canteenId") Long canteenId);

}
