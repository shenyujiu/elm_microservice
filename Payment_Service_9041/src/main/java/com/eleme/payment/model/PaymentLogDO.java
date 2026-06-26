package com.eleme.payment.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentLogDO {
    private Long id;
    private Long orderId;
    private String orderNo;
    private String payChannel;
    private String payStatus;
    private String transactionNo;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private LocalDateTime callbackAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public String getPayChannel() { return payChannel; }
    public void setPayChannel(String payChannel) { this.payChannel = payChannel; }
    public String getPayStatus() { return payStatus; }
    public void setPayStatus(String payStatus) { this.payStatus = payStatus; }
    public String getTransactionNo() { return transactionNo; }
    public void setTransactionNo(String transactionNo) { this.transactionNo = transactionNo; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getCallbackAt() { return callbackAt; }
    public void setCallbackAt(LocalDateTime callbackAt) { this.callbackAt = callbackAt; }
}
