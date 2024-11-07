package com.ONE4ALL.MFU_Canteen.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ONE4ALL.MFU_Canteen.Entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername(String username);
    boolean existsByUsername(String username);
    
}
