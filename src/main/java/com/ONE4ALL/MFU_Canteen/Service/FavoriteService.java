package com.ONE4ALL.MFU_Canteen.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ONE4ALL.MFU_Canteen.Entity.FavoriteItem;
import com.ONE4ALL.MFU_Canteen.Entity.Item;
import com.ONE4ALL.MFU_Canteen.Entity.User;
import com.ONE4ALL.MFU_Canteen.Repository.FavoriteItemRepository;
import com.ONE4ALL.MFU_Canteen.Repository.ItemRepository;
import com.ONE4ALL.MFU_Canteen.Repository.UserRepository;

@Service
public class FavoriteService {
    
    @Autowired
    private FavoriteItemRepository favoriteItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    public void toggleFavorite(Long userId, Long itemId){
        Optional<FavoriteItem> existingFavorite = favoriteItemRepository.findByUserIdAndItem_ItemId(userId, itemId);

        if(existingFavorite.isPresent()){
            //Remove favorite it it already exists
            favoriteItemRepository.delete(existingFavorite.get());
        }else{
            //Add favorite if it doesn't exist
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found")); 

            FavoriteItem favoriteItem = new FavoriteItem(user, item);
            favoriteItemRepository.save(favoriteItem);
        }
    }

    public List<FavoriteItem> getFavoritesByUser(Long userId){
        return favoriteItemRepository.findByUserId(userId);
    }
}
