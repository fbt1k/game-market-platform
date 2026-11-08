package com.example.market.market.service;

import com.example.market.market.model.Item;
import com.example.market.market.model.MarketLot;
import com.example.market.market.repository.ItemRepository;
import com.example.market.market.repository.MarketLotRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;


@Service
public class MarketService {
    
    private final ItemRepository itemRepository;
    private final MarketLotRepository marketLotRepository;
    private final RestTemplate restTemplate;

    public MarketService(ItemRepository itemRepository, MarketLotRepository marketLotRepository, RestTemplate restTemplate){
        this.itemRepository = itemRepository;
        this.marketLotRepository = marketLotRepository;
        this.restTemplate = restTemplate;
    }

    public Item creaItem(Item item){
        return itemRepository.save(item);
    }
    
    public MarketLot createLot(MarketLot lot){
        lot.setStats("ACTIVE");
        return  marketLotRepository.save(lot);
    }

    public List<MarketLot> getActiveLots(){
        return marketLotRepository.findByStatus("ACTIVE");
    }
    //логика покупки
    public void buyItem(Long lotId, Long buyerId){
        MarketLot lot = marketLotRepository.findById(lotId)
            .orElseThrow(() -> new RuntimeException("Лот не найден"));

        if (!"ACTIVE".equals(lot.getStatus())){
            throw new RuntimeException("Этот лот закрыт или отменен");
        }

    

    Item item = itemRepository.findById(lot.getItemId())
            .orElseThrow(() -> new RuntimeException("Предмет не найден"));
    
    // http put запрос в user service списать деньги
    String decreaseUrl = "http://users-service:8081/api/users/" + buyerId + "/balance?amount=" + lot.getPrice().negate();
        try {
            restTemplate.put(decreaseUrl, null);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка проведения сделки: Не хватает золота или пользователь не найден!");
        }
    
    String salesUrl = "http://users-service:8081/api/users/" + lot.getSellerId() + "/balance?amount=" + lot.getPrice();
    restTemplate.put(salesUrl, null);
    
    lot.setStatus("SOLD");
    marketLotRepository.save(lot);
    
    String logMessage = String.format("Игрок ID %d успешно купил '%s' за %s монет у игрока ID %d.",
             buyerId, item.getTitle(), lot.getPrice().toString(), lot.getSellerId());
    
    }
    // http post запрос в севис уведомлений
    String notificationUrl = "http://notifications-service:8083/api/notifications";
    try{
        restTemplate.postForEntity(NotificationUrl, logMessage, Void.class);
    } catch (Exception e){
        System.out.println("Сервис недоступен, сделка зафиксирована в базе данных");
    }
}
