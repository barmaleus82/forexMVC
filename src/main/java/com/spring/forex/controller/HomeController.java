package com.spring.forex.controller;

import com.spring.forex.model.RateFile;
import com.spring.forex.service.CurrencyService;
import com.spring.forex.service.PeriodService;
import com.spring.forex.service.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {
    private CurrencyService currencyService;
    private PeriodService periodService;
    private FileService rateFileService;

    public HomeController(CurrencyService currencyService, PeriodService periodService, FileService rateFileService) {
        this.currencyService = currencyService;
        this.periodService = periodService;
        this.rateFileService = rateFileService;
    }

    @GetMapping({"/","home"})
    public String create(Model model) {
        List<RateFile> fileList = rateFileService.getAll();
        Collections.sort(fileList);
        model.addAttribute("files", fileList);
        model.addAttribute("periods", periodService.getAll());
        model.addAttribute("currencies", currencyService.getAll());
        return "home";
    }
}
