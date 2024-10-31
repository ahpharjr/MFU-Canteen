package com.ONE4ALL.MFU_Canteen.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {
    
    @Id
    private Long userId;
    private String name;
    private String password;
    private String email;
    private String phNum;
    private String profilePicture;

    public User(){
        
    }

    // @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    // private Cart cart;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    // public Cart getCart() {
    //     return cart;
    // }

    // public void setCart(Cart cart) {
    //     this.cart = cart;
    // }

    
}
