package com.spring.forex.service;

import com.spring.forex.model.Currency;
import com.spring.forex.model.Period;
import com.spring.forex.model.Rate;

import java.time.LocalDateTime;
import java.util.List;

public interface RateService {
    Rate create(Rate rate);
    Rate readById(long id);
    Rate update(Rate rate);
    void delete(long id);
    List<Rate> getAll();
    Rate getByDateTimeCurrencyPeriod(LocalDateTime dateTime, Currency currency, Period period);
}
