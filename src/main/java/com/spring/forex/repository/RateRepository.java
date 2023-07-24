package com.spring.forex.repository;

import com.spring.forex.model.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long>{
    @Query("from Rate where dateTime = :date_time and currency.id = :currency_id and period.id = :period_id")
    Rate getByDateTimeCurrencyIdPeriodId(LocalDateTime date_time, long currency_id, long period_id);
    @Query("from Rate where currency.id = :currency_id and period.id = :period_id")
    List<Rate> getByCurrencyIdPeriodId(long currency_id, long period_id);
}
