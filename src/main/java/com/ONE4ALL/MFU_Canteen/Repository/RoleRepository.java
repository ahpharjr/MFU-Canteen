package com.ONE4ALL.MFU_Canteen.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ONE4ALL.MFU_Canteen.Entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{
     Role findByName(String name);
}
