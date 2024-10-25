package com.ONE4ALL.MFU_Canteen.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ONE4ALL.MFU_Canteen.Entity.Owner;
import com.ONE4ALL.MFU_Canteen.Entity.Shop;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long>{
    // List<Shop> findByOwnerId(Long ownerId);
    
}
