package com.spring.forex.service.impl;

import com.spring.forex.model.Currency;
import com.spring.forex.model.Period;
import com.spring.forex.model.Rate;
import com.spring.forex.repository.PeriodRepository;
import com.spring.forex.repository.RateRepository;
import com.spring.forex.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RateServiceImpl implements RateService {
    @Autowired
    private RateRepository rateRepository;

    @Override
    public Rate create(Rate rate) {
        try{
            return rateRepository.save(rate);
        }catch (RuntimeException ex){
            throw new RuntimeException("Rate doesn't create");
        }
    }

    @Override
    public Rate readById(long id) {
        Optional<Rate> optional = rateRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new EntityNotFoundException("Rate with id=" + id + " not found");
    }

    @Override
    public Rate update(Rate rate) {
        return null;
    }

    @Override
    public void delete(long id) {
        Rate rate = readById(id);
        if (rate != null) {
            rateRepository.delete(rate);
        } else {
            throw new EntityNotFoundException("Period with id " + id + " not found");
        }
    }

    @Override
    public List<Rate> getAll() {
        List<Rate> rateList = rateRepository.findAll();
        return rateList.isEmpty() ? new ArrayList<>() : rateList;
    }

    @Override
    public Rate getByDateTimeCurrencyPeriod(LocalDateTime dateTime, Currency currency, Period period) {
        return rateRepository.getByDateTimeCurrencyIdPeriodId(dateTime,currency.getId(),period.getId());
    }
}
