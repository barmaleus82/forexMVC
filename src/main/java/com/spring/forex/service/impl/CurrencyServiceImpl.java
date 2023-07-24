package com.spring.forex.service.impl;

import com.spring.forex.model.Currency;
import com.spring.forex.repository.CurrencyRepository;
import com.spring.forex.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Override
    public Currency create(Currency currency) {
        try{
            return currencyRepository.save(currency);
        }catch (RuntimeException ex){
            throw new RuntimeException("Currency doesn't create");
        }
    }

    @Override
    public Currency readById(long id) {
        Optional<Currency> optional = currencyRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new EntityNotFoundException("Currency with id=" + id + " not found");
    }

    @Override
    public Currency update(Currency currency) {
        if (currency != null && currency.getName() != "") {
            Currency oldCurrency = readById(currency.getId());
            if (oldCurrency != null && oldCurrency.getName() != "") {
                return currencyRepository.save(currency);
            }
        }
        throw new RuntimeException("Currency cannot be 'null'");
    }

    @Override
    public void delete(long id) {
        Currency currency = readById(id);
        if (currency != null) {
            currencyRepository.delete(currency);
        } else {
            throw new EntityNotFoundException("Currency with id " + id + " not found");
        }
    }

    @Override
    public List<Currency> getAll() {
        List<Currency> currList = currencyRepository.findAll();
        return currList.isEmpty() ? new ArrayList<>() : currList;
    }

    @Override
    public Currency getByName(String currencyName) {
        return currencyRepository.getByName(currencyName);
    }
}
