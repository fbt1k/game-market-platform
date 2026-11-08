package com.example.market.market.controller;

import com.example.market.market.model.Item;
import com.example.market.market.model.MarketLot;
import com.example.market.market.service.MarketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestContrelller
@RequestMapping("/api/market")
public class MarketController{

    private final MarketService marketService;

    public MarketController(MarketService marketService){
        this.marketService = marketService;
    }
    //создать предмет 
    // url  для вызова POST http://localhost:8080/api/market/items
    @PostMapping("/items")
    public ResponseEntity<item> createItem(@RequestBody Item item){
        return ResponseEntity.ok(marketService.createItem(item));
    }
    // выставить на продажу
    //POST http://localhost:8080/api/market/lots
    @PostMapping("/lots")
    public ResponseEntity<MarketLot> createLot(@RequestBody MarketLot lot){
        return ResponseEntity.ok(marketService.createLot(lot));
    }
    // просмотр всех активынх лотов
    //GET http://localhost:8080/api/market/lots
    @GetMapping("/lots")
    public ResponseEntity<List<MarketLot>> getActiveLots(){
        return ResponseEntity.ok(marketService.getActiveLots());
    }
    // выкупить лот
    //POST http://localhost:8080/api/market/lots/{lotId}/buy
    @PostMapping ("/lots/{lotId}/buy")
    public ResponseEntity <Void> buyItem(@PathVariable Long lotId, @RequestBody Map<String, Long> payload){
        Long buyerId = payload.get("buyerId");
        if (buyerId==null){
            return ResponseEntity.badRequest().build();
        }
        marketService.buyItem(lotId, buyerId);
        return  ResponseEntity.ok().build();
    }
}