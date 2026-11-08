package com.example.market.market.model;



import jakarta.persistence.*;

import java.math.BigDecimal;



@Entity
@Table(name = "market_lots")
public class MarketLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long itemId;
    private Long sellerId;
    private BigDecimal price;
    private String status; // active/sold/canceled

    public MarketLot(Long itemID, Long sellerId, BigDecimal price, String status){
        this.itemId = itemId;
        this.sellerId = sellerId;
        this.price = price;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
