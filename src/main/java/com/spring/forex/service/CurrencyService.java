package com.spring.forex.service;

import com.spring.forex.model.Currency;

import java.util.List;

public interface CurrencyService {
    Currency create(Currency currency);
    Currency readById(long id);
    Currency update(Currency currency);
    void delete(long id);
    List<Currency> getAll();
    Currency getByName(String currencyName);
}
