package com.spring.forex.controller;

import com.spring.forex.model.Currency;
import com.spring.forex.model.Period;
import com.spring.forex.service.CurrencyService;
import com.spring.forex.service.FileService;
import com.spring.forex.service.PeriodService;
import com.spring.forex.service.RateService;
import com.spring.forex.service.jdbc.RateJDBCServiceImpl;
import com.spring.forex.analitic.Analitic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;


@Controller
@RequestMapping("/analitics")
public class AnaliticsController {
    @Autowired
    private FileService rateFileService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private PeriodService periodService;
    @Autowired
    private RateService rateService;
    @Autowired
    private Analitic statistics;


    @Autowired
    private RateJDBCServiceImpl rateJDBCService;


    @GetMapping
    public String analiticsHome() {
        return "analitics";
    }

    @GetMapping("/activity/{period_step}")
    public String statistic(Model model, @PathVariable("period_step") int period_step) {
        Period period = periodService.getByStep(period_step);

        Map<Currency, List<List<String>>> resultMap = new TreeMap<>();
        for (Currency curr : currencyService.getAll()) {
            resultMap.put(curr, statistics.getStatistic(curr, period));
        }
        model.addAttribute("period", "Activity " + period.getName());
        model.addAttribute("map", resultMap);

        return "analitics-activity";
    }

    @GetMapping("/period-max-min/{period_step}")
    public String statisticPeriodMaxMin(Model model, @PathVariable("period_step") int period_step) {
        Period period = periodService.getByStep(period_step);

        for (Currency curr : currencyService.getAll()) {
            statistics.openClosePair(curr, period);
        }
        return "redirect:/home";
    }

    @GetMapping("/average-max-min-open-close/{period_step}")
    public String averageMaxMinOpenClose(Model model, @PathVariable("period_step") int period_step) {
        Period period = periodService.getByStep(period_step);

        Map<Currency, String> resultMap = new TreeMap<>();
        List<Integer> hours = Arrays.asList(7,8,9,10); //2,3,4,
        for (Currency curr : currencyService.getAll()) {
            resultMap.put(curr, statistics.averageMaxMinOpenClose(curr, period, hours));
        }

        for (Currency curr : resultMap.keySet()){
            System.out.println(curr.getName()+": "+resultMap.get(curr));
        }

        return "redirect:/home";
    }
}
