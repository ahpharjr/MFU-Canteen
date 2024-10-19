package com.ONE4ALL.MFU_Canteen.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Canteen {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long canteenId;
    private String name;
    private String location;

    public Canteen(){
        
    }

    public Long getCanteenId() {
        return canteenId;
    }
    public void setCanteenId(Long canteenId) {
        this.canteenId = canteenId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    
    
}
