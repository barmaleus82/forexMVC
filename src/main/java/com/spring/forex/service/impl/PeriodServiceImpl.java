package com.spring.forex.service.impl;

import com.spring.forex.model.Currency;
import com.spring.forex.model.Period;
import com.spring.forex.repository.PeriodRepository;
import com.spring.forex.service.PeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Access;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PeriodServiceImpl implements PeriodService {

    @Autowired
    private PeriodRepository periodRepository;

    @Override
    public Period create(Period period) {
        try{
            return periodRepository.save(period);
        }catch (RuntimeException ex){
            throw new RuntimeException("Period doesn't create");
        }
    }

    @Override
    public Period readById(long id) {
        Optional<Period> optional = periodRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new EntityNotFoundException("Period with id=" + id + " not found");
    }

    @Override
    public Period update(Period period) {
        if (period != null && period.getName() != "") {
            Period oldPeriod = readById(period.getId());
            if (oldPeriod != null && oldPeriod.getName() != "") {
                return periodRepository.save(period);
            }
        }
        throw new RuntimeException("Period cannot be 'null'");
    }

    @Override
    public void delete(long id) {
        Period period = readById(id);
        if (period != null) {
            periodRepository.delete(period);
        } else {
            throw new EntityNotFoundException("Period with id " + id + " not found");
        }
    }

    @Override
    public List<Period> getAll() {
        List<Period> periodList = periodRepository.findAll();
        return periodList.isEmpty() ? new ArrayList<>() : periodList;
    }

    @Override
    public Period getByName(String periodName) {
        return periodRepository.getByName(periodName);
    }

    @Override
    public Period getByStep(int step) {
        return periodRepository.getByStep(step);
    }
}
