package com.spring.forex.service;

import com.spring.forex.model.Period;

import java.util.List;

public interface PeriodService {
    Period create(Period period);
    Period readById(long id);
    Period update(Period period);
    void delete(long id);
    List<Period> getAll();
    Period getByName(String periodName);
    Period getByStep(int step);
}
