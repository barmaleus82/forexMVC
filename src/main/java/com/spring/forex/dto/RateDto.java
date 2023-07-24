package com.spring.forex.dto;

import com.spring.forex.model.Rate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter @Setter
public class RateDto {
    private long id;
    private long currency_id;
    private long period_id;
    private LocalDateTime dateTime;
    private double open;
    private double max;
    private double min;
    private double close;
    private int volume;
    private int spred;

    public RateDto(Rate rate) {
        this.id = rate.getId();
        this.currency_id = rate.getCurrency() == null ? 0 : rate.getCurrency().getId();
        this.period_id = rate.getPeriod() == null ? 0 : rate.getPeriod().getId();
        this.dateTime = rate.getDateTime();
        this.open = rate.getOpen();
        this.max = rate.getMax();
        this.min = rate.getMin();
        this.close = rate.getClose();
        this.volume = rate.getVolume();
        this.spred = rate.getSpread();
    }
}
