package com.spring.forex.repository;

import com.spring.forex.model.RateFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RateFileRepository extends JpaRepository<RateFile, Long>{
//    @Query("from RateFile where currency.id = :currencyId and period.id = :periodId")
//    File getByCurrencyIdAndPeriodId(long currencyId, long periodId);
//
    @Query("from RateFile where fileName = :file_name")
    RateFile getByFileName(String file_name);
}
