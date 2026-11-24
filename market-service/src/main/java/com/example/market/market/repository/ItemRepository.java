package com.example.market.market.repository;

import com.example.market.market.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  ItemRepository extends JpaRepository<Item, Long>{
    
}



