package com.ONE4ALL.MFU_Canteen.Entity;


import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
// import jakarta.persistence.OneToOne;

@Entity
public class Owner {
    
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long ownerId;
    private String name;
    private String password;
    private String email;
    private String phNum;
    private String profilePicture;

    // @OneToOne(mappedBy = "owner")
    // private Shop shop;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Shop> shops = new ArrayList<>();
    
    public Owner(){

    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhNum() {
        return phNum;
    }

    public void setPhNum(String phNum) {
        this.phNum = phNum;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    // public Shop getShop() {
    //     return shop;
    // }

    // public void setShop(Shop shop) {
    //     this.shop = shop;
    // };


}
