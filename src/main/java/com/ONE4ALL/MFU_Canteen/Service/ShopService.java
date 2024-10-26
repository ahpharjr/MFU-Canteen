package com.ONE4ALL.MFU_Canteen.Service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ONE4ALL.MFU_Canteen.Entity.Item;
import com.ONE4ALL.MFU_Canteen.Entity.Shop;
import com.ONE4ALL.MFU_Canteen.Repository.ShopRepository;

@Service
public class ShopService {
    
    @Autowired
    private ShopRepository shopRepository;

    public Shop getShopById(Long shopId){
        return shopRepository.findById(shopId).orElseThrow(() -> new IllegalArgumentException("Invalid shop ID"));
    }

    public List<Shop> getShopsByOwner(Long ownerId) {
        return shopRepository.findByOwner_OwnerId(ownerId); // Ensure this method is correctly defined
    }

    public void updateShop(Long shopId, Shop updateShop){
        Shop existingShop = getShopById(shopId);
        existingShop.setName(updateShop.getName());
        existingShop.setDescription(updateShop.getDescription());
        existingShop.setPhNum(updateShop.getPhNum());
        existingShop.setShopNum(updateShop.getShopNum());

        shopRepository.save(existingShop);
    }


    // public List<Item> getRecommendedItemsByCanteen(Long canteenId) {
    //     List<Shop> shops = shopRepository.findByCanteen_CanteenId(canteenId); // Method to get shops by canteen ID
    //     List<Item> recommendedItems = new ArrayList<>();

    //     for (Shop shop : shops) {
    //         recommendedItems.addAll(shop.getItems()); // Assuming there's a method to get items from a shop
    //     }

    //     return recommendedItems;
    // }

}
