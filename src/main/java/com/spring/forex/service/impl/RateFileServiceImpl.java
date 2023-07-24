package com.spring.forex.service.impl;

import com.spring.forex.model.RateFile;
import com.spring.forex.repository.CurrencyRepository;
import com.spring.forex.repository.PeriodRepository;
import com.spring.forex.repository.RateFileRepository;
import com.spring.forex.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.util.List;

@Service
public class RateFileServiceImpl implements FileService {

    String rateFilesDir = "src/main/resources/ratefiles";
    @Autowired
    private RateFileRepository fileRepository;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private PeriodRepository periodRepository;


    @Override
    public RateFile create(RateFile file) {
        if (fileRepository.getReferenceById(file.getId())==file){
            return null;
        }
        return fileRepository.save(file);
    }

    @Override
    public RateFile readById(long id) {
        return fileRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Role with id " + id + " not found"));
    }

    @Override
    public RateFile update(RateFile file) {
        if (file != null) {
            readById(file.getId());
            return fileRepository.save(file);
        }
        throw new EntityNotFoundException("Role cannot be 'null'");
    }

    @Override
    public void delete(long id) {
        RateFile file = readById(id);
        fileRepository.delete(file);
    }

    @Override
    public List<RateFile> getAll() {
        return fileRepository.findAll();
    }

//    @Override
//    public File getByCurrencyAndPeriod(Currency currency, Period period) {
//        return fileRepository.getByCurrencyIdAndPeriodId(currency.getId(),period.getId());
//    }

    @Override
    public RateFile getByFileName(String fileName) {
        return fileRepository.getByFileName(fileName);
    }

    @Override
    public void searchFilesFromDir() {
        File directory = new File(rateFilesDir);
        if (directory.exists() && directory.isDirectory()) {
            for (java.io.File file : directory.listFiles()) {
                RateFile findFile = getByFileName(file.getName());
                if (findFile == null){
                    String fileName = file.getName();

                    RateFile newRateFile = new RateFile();
                    newRateFile.setFileName(fileName);
                    newRateFile.setCurrency(currencyRepository.getByName(fileName.substring(0,6)));
                    String periodStr = fileName.substring(6,fileName.indexOf('.'));
                    switch (periodStr){
                        case "M1":
                            newRateFile.setPeriod(periodRepository.getByName("1 min"));
                            break;
                        case "M5":
                            newRateFile.setPeriod(periodRepository.getByName("5 min"));
                            break;
                        case "M15":
                            newRateFile.setPeriod(periodRepository.getByName("15 min"));
                            break;
                        case "M30":
                            newRateFile.setPeriod(periodRepository.getByName("30 min"));
                            break;
                        case "H1":
                            newRateFile.setPeriod(periodRepository.getByName("1 hour"));
                            break;
                    }
                    create(newRateFile);
                }
            }
        } else {
            System.out.println("Invalid directory path.");
        }
    }
}
