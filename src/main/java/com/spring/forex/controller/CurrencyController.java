package com.spring.forex.controller;

import com.spring.forex.model.Currency;
import com.spring.forex.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

@Controller
@RequestMapping("/currency")
public class CurrencyController {
    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("currency", new Currency());
        return "create-currency";
    }

    @PostMapping("/create")
    public String create(Model model,
                         @Validated @ModelAttribute("currency") Currency currency,
                         BindingResult result) {
        if (!result.hasErrors()) {
            Currency newCurr = currencyService.create(currency);
            if (newCurr != null) {
                return "redirect:/home";
            }
        }
        return "create-currency";
    }

    @GetMapping("/{curr_id}/update")
    public String update(Model model,
                         @PathVariable("curr_id") long currId) {
        Currency findCurr = currencyService.readById(currId);
        model.addAttribute("currency", findCurr);
        return "update-currency";
    }

    @PostMapping("/update")
    public String update(Model model,
                         @Validated @ModelAttribute("currency") Currency currency,
                         BindingResult result){
        if(!result.hasErrors()){
            Currency findCurr = currencyService.readById(currency.getId());
            findCurr.setName(currency.getName());
            findCurr.setSpread(currency.getSpread());
            findCurr.setSwap(currency.getSwap());
            findCurr.setPointRatio(currency.getPointRatio());

            Currency updatedCurr = currencyService.update(findCurr);
            if (updatedCurr != null) {
                return "redirect:/home";
            }
        }
        return "update-currency";
    }

    @GetMapping("/{curr_id}/delete")
    public String delete(Model model, @PathVariable("curr_id") long currId) {
        model.addAttribute("message", "Delete Currency: "+currencyService.readById(currId).getName()+" ?");
        model.addAttribute("backLink", "/currency/"+currId+"/delete/conform");
        return "conform";
    }

    @GetMapping("/{curr_id}/delete/conform")
    public String deleteConform(@PathVariable("curr_id") long currId) {
        currencyService.delete(currId);
        return "redirect:/home";
    }
}
