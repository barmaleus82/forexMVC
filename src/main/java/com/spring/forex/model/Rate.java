package com.spring.forex.model;

import com.spring.forex.dto.RateDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "rates")
public class Rate implements Comparable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="currency_id", nullable=false)
    private Currency currency;

    @ManyToOne
    @JoinColumn(name="period_id", nullable=false)
    private Period period;

    @Column(name="date_time",nullable = false)
    private LocalDateTime dateTime;

    @Column
    private double open;
    @Column
    private double max;
    @Column
    private double min;
    @Column
    private double close;
    @Column
    private int volume;
    @Column
    private int spread;

    public void setFromDTO(RateDto rateDto, Currency currency, Period period){
        this.currency = currency;
        this.period = period;
        this.dateTime = rateDto.getDateTime();
        this.open = rateDto.getOpen();
        this.max = rateDto.getMax();
        this.min = rateDto.getMin();
        this.close = rateDto.getClose();
        this.volume = rateDto.getVolume();
        this.spread = rateDto.getSpred();
    }

    @Override
    public int compareTo(Object obj) {
        if (obj != null && obj instanceof Rate){
            Rate compRate = (Rate) obj;
            if (currency.getId() != compRate.currency.getId()){
                return (int)(currency.getId() - compRate.currency.getId());
            }else{
                return dateTime.compareTo(compRate.dateTime);
            }
        }
        return -1;
    }
}
