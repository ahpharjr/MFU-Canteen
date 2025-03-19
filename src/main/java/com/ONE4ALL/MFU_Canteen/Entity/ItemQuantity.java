package com.ONE4ALL.MFU_Canteen.Entity;

public class ItemQuantity {
    private Long itemId;
    private Long quantity;

    // Constructor, Getters, and Setters
    public ItemQuantity() {}

    public ItemQuantity(Long itemId, Long quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
