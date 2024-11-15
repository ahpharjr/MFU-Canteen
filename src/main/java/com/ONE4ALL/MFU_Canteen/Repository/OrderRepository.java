package com.ONE4ALL.MFU_Canteen.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ONE4ALL.MFU_Canteen.Entity.Order;
import com.ONE4ALL.MFU_Canteen.Entity.Shop;
import com.ONE4ALL.MFU_Canteen.Entity.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, String>{
    List<Order> findByUser(User user);
    List<Order> findByUserOrderByOrderDateDesc(User user);

    List<Order> findByShop(Shop shop);
    List<Order> findByShopIn(List<Shop> shops);
    List<Order> findByShopInAndStatus(List<Shop> shops, String status);
}
