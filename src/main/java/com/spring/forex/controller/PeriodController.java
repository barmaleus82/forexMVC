package com.spring.forex.controller;

import com.spring.forex.model.Period;
import com.spring.forex.service.PeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/period")
public class PeriodController {
    @Autowired
    private PeriodService periodService;

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("period", new Period());
        return "create-period";
    }

    @PostMapping("/create")
    public String create(Model model,
                         @Validated @ModelAttribute("period") Period period,
                         BindingResult result) {
        if (!result.hasErrors()) {
            Period newPeriod = periodService.create(period);
            if (newPeriod != null) {
                return "redirect:/home";
            }
        }
        return "create-period";
    }

    @GetMapping("/{period_id}/update")
    public String update(Model model,
                         @PathVariable("period_id") long periodId) {
        Period findPeriod = periodService.readById(periodId);
        model.addAttribute("period", findPeriod);
        return "update-period";
    }

    @PostMapping("/update")
    public String update(Model model,
                         @Validated @ModelAttribute("period") Period period,
                         BindingResult result){
        if(!result.hasErrors()){
            Period findPeriod = periodService.readById(period.getId());
            findPeriod.setName(period.getName());
            findPeriod.setStep(period.getStep());

            Period updatedPeriod = periodService.update(findPeriod);
            if (updatedPeriod != null) {
                return "redirect:/home";
            }
        }
        return "update-period";
    }

    @GetMapping("/{period_id}/delete")
    public String deleteGet(Model model, @PathVariable("period_id") long periodId) {
        model.addAttribute("message", "Delete Period: "+periodService.readById(periodId).getName()+" ?");
        model.addAttribute("backLink", "/period/"+periodId+"/delete/conform");
        return "conform";
    }

    @GetMapping("/{period_id}/delete/conform")
    public String deletePost(@PathVariable("period_id") long periodId) {
        periodService.delete(periodId);
        return "redirect:/home";
    }
}
