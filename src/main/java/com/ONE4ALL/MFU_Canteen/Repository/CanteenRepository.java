package com.ONE4ALL.MFU_Canteen.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ONE4ALL.MFU_Canteen.Entity.Canteen;

@Repository
public interface CanteenRepository extends JpaRepository<Canteen, Long>{
    
}
