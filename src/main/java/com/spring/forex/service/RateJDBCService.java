package com.spring.forex.service;

import com.spring.forex.model.RateFile;

public interface RateJDBCService {
    void fillRateFromFile(RateFile rateFile);
}
