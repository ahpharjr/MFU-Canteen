package com.ONE4ALL.MFU_Canteen.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ONE4ALL.MFU_Canteen.Entity.FavoriteItem;

@Repository
public interface FavoriteItemRepository extends JpaRepository<FavoriteItem, Long>{

    List<FavoriteItem> findByUserId(Long userId);
    Optional<FavoriteItem> findByUserIdAndItem_ItemId(Long userId, Long itemId);
}
