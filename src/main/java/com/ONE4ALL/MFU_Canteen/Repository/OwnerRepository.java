package com.ONE4ALL.MFU_Canteen.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ONE4ALL.MFU_Canteen.Entity.Owner;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long>{
    // List<Shop> findByOwnerId(Long ownerId);
    
}
