package com.ONE4ALL.MFU_Canteen.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ONE4ALL.MFU_Canteen.Entity.Item;
import com.ONE4ALL.MFU_Canteen.Entity.Shop;
import com.ONE4ALL.MFU_Canteen.Repository.ItemRepository;
import com.ONE4ALL.MFU_Canteen.Repository.ShopRepository;

@Service
public class ItemService {
    
    @Autowired
    private ItemRepository itemRepository;

    @Autowired 
    private ShopRepository shopRepository;


    // public void saveItem(Item item){
    //     itemRepository.save(item);
    // }

    public void saveItem(Item item, Long shopId) {
    // Find the shop by ID
    Shop shop = shopRepository.findById(shopId)
            .orElseThrow(() -> new IllegalArgumentException("Shop not found with ID: " + shopId));
    
    // Associate the item with the shop
    item.setShop(shop);
    itemRepository.save(item);
    }
    
    public List<Item> getAllItems(){
        return itemRepository.findAll();
    }

    public List<Item> getItemsByShop(Long shopId) {
        return itemRepository.findByShop_ShopId(shopId); // Assuming you have this method in your ItemRepository
    }

}
