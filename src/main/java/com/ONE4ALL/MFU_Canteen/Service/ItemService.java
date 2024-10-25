package com.ONE4ALL.MFU_Canteen.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ONE4ALL.MFU_Canteen.Entity.Item;
import com.ONE4ALL.MFU_Canteen.Repository.ItemRepository;

@Service
public class ItemService {
    
    @Autowired
    private ItemRepository itemRepository;


    public void saveItem(Item item){
        itemRepository.save(item);
    }

    public List<Item> getAllItems(){
        return itemRepository.findAll();
    }

    public List<Item> getItemsByShop(Long shopId) {
        return itemRepository.findByShop_ShopId(shopId); // Assuming you have this method in your ItemRepository
    }

}
