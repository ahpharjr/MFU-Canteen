package com.ONE4ALL.MFU_Canteen.Entity;

import java.util.List;

public class CheckoutRequest {
    private List<ItemQuantity> items;

    // Getter and Setter
    public List<ItemQuantity> getItems() {
        return items;
    }

    public void setItems(List<ItemQuantity> items) {
        this.items = items;
    }
}
