package com.example.market.market.repository;

import com.example.market.market.model.MarketLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MarketLotRepository extends JpaRepository<MarketLot, Long> {
    // Метод для получения только активных лотов на продажу
    List<MarketLot> findByStatus(String status);
}