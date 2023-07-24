package com.spring.forex.dto;

import com.spring.forex.model.RateFile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter @Setter
public class FileDto {
    private long id;
    private String file_name;
    private long currency_id;
    private long period_id;
    private LocalDateTime last_upload;

    public FileDto(RateFile rateFile){
        this.id = rateFile.getId();
        this.file_name = rateFile.getFileName();
        this.currency_id = rateFile.getCurrency() == null ? 0 : rateFile.getCurrency().getId();
        this.period_id = rateFile.getPeriod() == null ? 0 : rateFile.getPeriod().getId();
        this.last_upload = rateFile.getLastUploaded();

    }
}
