package com.spring.forex.repository;

import com.spring.forex.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long>{
    @Query("from Currency where name = :currencyName")
    Currency getByName(String currencyName);
}
