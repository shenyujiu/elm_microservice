package com.eleme.order.model;

import java.math.BigDecimal;

public class OrderItemDO {
    private Long id;
    private Long orderId;
    private Long foodId;
    private String foodName;
    private BigDecimal foodPrice;
    private Integer quantity;
    private BigDecimal amount;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getFoodId() { return foodId; }
    public void setFoodId(Long foodId) { this.foodId = foodId; }
    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }
    public BigDecimal getFoodPrice() { return foodPrice; }
    public void setFoodPrice(BigDecimal foodPrice) { this.foodPrice = foodPrice; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
