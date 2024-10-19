package com.ONE4ALL.MFU_Canteen.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ONE4ALL.MFU_Canteen.Entity.Shop;
import com.ONE4ALL.MFU_Canteen.Repository.ShopRepository;

@Service
public class ShopService {
    
    @Autowired
    private ShopRepository shopRepository;

    public Shop getShopById(Long shopId){
        return shopRepository.findById(shopId).orElseThrow(() -> new IllegalArgumentException("Invalid shop ID"));
    }

    public void updateShop(Long shopId, Shop updateShop){
        Shop existingShop = getShopById(shopId);
        existingShop.setName(updateShop.getName());
        existingShop.setDescription(updateShop.getDescription());
        existingShop.setPhNum(updateShop.getPhNum());
        existingShop.setShopNum(updateShop.getShopNum());
        // existingShop.setPicture(updateShop.getPicture());

        shopRepository.save(existingShop);
    }
}
