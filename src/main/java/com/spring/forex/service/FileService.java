package com.spring.forex.service;

import com.spring.forex.model.RateFile;

import java.util.List;

public interface FileService {
    RateFile create(RateFile file);
    RateFile readById(long id);
    RateFile update(RateFile file);
    void delete(long id);
    List<RateFile> getAll();
//    File getByCurrencyAndPeriod(Currency currency, Period period);
    RateFile getByFileName(String fileName);
    void searchFilesFromDir();
}
