package com.ONE4ALL.MFU_Canteen.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Shop {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long shopId;

    private String name;

    @Column(name = "shop_num")
    private String shopNum;
    private String picture;
    private String description;
    
    @Column(name = "ph_num")
    private String phNum;

    @ManyToOne
    @JoinColumn(name = "canteenId", nullable = false)
    private Canteen canteen;

    @ManyToOne
    @JoinColumn(name = "ownerId", nullable = false)
    private Owner owner;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    // public Long getOwnerId() {
    //     return ownerId;
    // }

    // public void setOwnerId(Long ownerId) {
    //     this.ownerId = ownerId;
    // }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShopNum() {
        return shopNum;
    }

    public void setShopNum(String shopNum) {
        this.shopNum = shopNum;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhNum() {
        return phNum;
    }

    public void setPhNum(String phNum) {
        this.phNum = phNum;
    }

    public Canteen getCanteen() {
        return canteen;
    }

    public void setCanteen(Canteen canteen) {
        this.canteen = canteen;
    }

    
}
