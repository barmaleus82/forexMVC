package com.spring.forex.controller;

import com.spring.forex.model.RateFile;
import com.spring.forex.service.CurrencyService;
import com.spring.forex.service.PeriodService;
import com.spring.forex.service.FileService;
import com.spring.forex.service.RateService;
import com.spring.forex.service.jdbc.RateJDBCServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rates")
public class RateController {
    @Autowired
    private FileService rateFileService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private PeriodService periodService;
    @Autowired
    private RateService rateService;

    @Autowired
    private RateJDBCServiceImpl rateJDBCService;

    @GetMapping("/upload")
    public String upload() {
        for (RateFile file : rateFileService.getAll()){
            rateJDBCService.fillRateFromFile(file);
        }
        return "redirect:/home";
    }

    @GetMapping("/{file_id}/upload")
    public String upload(@PathVariable("file_id") long fileId) {
        rateJDBCService.fillRateFromFile(rateFileService.readById(fileId));
        return "redirect:/home";
    }
}
