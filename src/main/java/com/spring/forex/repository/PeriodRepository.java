package com.spring.forex.repository;

import com.spring.forex.model.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PeriodRepository extends JpaRepository<Period, Long>{
    @Query("from Period where name = :periodName")
    Period getByName(String periodName);

    @Query("from Period where step = :step")
    Period getByStep(int step);
}
