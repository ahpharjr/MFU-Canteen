package com.ONE4ALL.MFU_Canteen.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ONE4ALL.MFU_Canteen.Entity.Owner;
import com.ONE4ALL.MFU_Canteen.Repository.OwnerRepository;

@Service
public class OwnerService {
    
    @Autowired
    private OwnerRepository ownerRepository;

    public List<Owner> getAllOwners(){
        return ownerRepository.findAll();
    }

    public Owner getOwnerById(Long ownerId){
        return ownerRepository.findById(ownerId).orElseThrow(() -> new IllegalArgumentException("Invalid owner Id"));
    }

    public Owner createOwner(Owner owner){
        return ownerRepository.save(owner);
    }

    public void deleteOwner(Long ownerId){
        ownerRepository.deleteById(ownerId);
    }

    public Owner updateOwner(Owner owner){
        return ownerRepository.save(owner);
    }

        // // If you need to fetch an Owner based on User
        // public Owner getOwnerByUser(User user) {
        //     return ownerRepository.findByUser(user);  // Assuming you have a query like this
        // }
}
