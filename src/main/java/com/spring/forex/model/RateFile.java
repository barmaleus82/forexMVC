package com.spring.forex.model;

import com.spring.forex.dto.FileDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "file_names")
public class RateFile implements Comparable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "file_name")
    @NotBlank
    private String fileName;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @ManyToOne
    @JoinColumn(name = "period_id")
    private Period period;

    @Column(name = "last_upload")
    private LocalDateTime lastUploaded;

    public void setFromDTO(FileDto rateFileDto, Currency currency, Period period) {
        this.fileName = rateFileDto.getFile_name();
        this.currency = currency;
        this.period = period;
        this.lastUploaded = rateFileDto.getLast_upload();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof RateFile))
            return false;
        RateFile other = (RateFile) o;
        return this.currency == other.currency &&
                this.period == other.period &&
                this.fileName == other.fileName;
    }

    @Override
    public int compareTo(Object obj) {
        RateFile compObj = (RateFile) obj;
        if (currency != null && period != null && compObj.getCurrency() != null && compObj.getPeriod() != null &&
                currency.getName().compareTo(compObj.getCurrency().getName()) == 0) {
            return period.getStep() - compObj.getPeriod().getStep();
        } else {
            return fileName.compareTo(compObj.getFileName());
        }
    }
}
