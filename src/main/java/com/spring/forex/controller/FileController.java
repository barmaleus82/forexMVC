package com.spring.forex.controller;

import com.spring.forex.dto.FileDto;
import com.spring.forex.model.RateFile;
import com.spring.forex.service.CurrencyService;
import com.spring.forex.service.PeriodService;
import com.spring.forex.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService rateFileService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private PeriodService periodService;

    @GetMapping("/search")
    public String searchFiles(Model model) {
        rateFileService.searchFilesFromDir();
        return "redirect:/home";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("file_dto", new FileDto());
        model.addAttribute("periods", periodService.getAll());
        model.addAttribute("currencies", currencyService.getAll());
        return "create-file";
    }

    @PostMapping("/create")
    public String create(Model model,
                         @Validated @ModelAttribute("file_dto") FileDto fileDto,
                         BindingResult result) {
        if (!result.hasErrors()) {
            RateFile newRateFile = new RateFile();
            newRateFile.setFromDTO(fileDto,
                    currencyService.readById(fileDto.getCurrency_id()),
                    periodService.readById(fileDto.getPeriod_id())
            );

            RateFile newFile = rateFileService.create(newRateFile);
            if (newFile != null) {
                return "redirect:/home";
            }
        }
        return "create-file";
    }

    @GetMapping("/{file_id}/update")
    public String update(Model model,
                         @PathVariable("file_id") long currId) {
        RateFile findFile = rateFileService.readById(currId);
        model.addAttribute("file_dto", new FileDto(findFile));
        model.addAttribute("periods", periodService.getAll());
        model.addAttribute("currencies", currencyService.getAll());
        return "update-file";
    }

    @PostMapping("/update")
    public String update(Model model,
                         @Validated @ModelAttribute("file_dto") FileDto fileDto,
                         BindingResult result){
        if(!result.hasErrors()){
            RateFile findFile = rateFileService.readById(fileDto.getId());
            findFile.setFromDTO(fileDto,
                    currencyService.readById(fileDto.getCurrency_id()),
                    periodService.readById(fileDto.getPeriod_id())
            );

            RateFile updatedFile = rateFileService.update(findFile);
            if (updatedFile != null) {
                return "redirect:/home";
            }
        }
        return "update-file";
    }

    @GetMapping("/{file_id}/delete")
    public String delete(Model model, @PathVariable("file_id") long fileId) {
        model.addAttribute("message", "Delete File: "+currencyService.readById(fileId).getName()+" ?");
        model.addAttribute("backLink", "/files/"+fileId+"/delete/conform");
        return "conform";
    }

    @GetMapping("/{file_id}/delete/conform")
    public String deleteConform(@PathVariable("file_id") long fileId) {
        rateFileService.delete(fileId);
        return "redirect:/home";
   }
}
