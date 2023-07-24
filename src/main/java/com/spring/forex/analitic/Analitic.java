package com.spring.forex.analitic;

import com.spring.forex.model.Currency;
import com.spring.forex.model.Period;
import com.spring.forex.model.Rate;
import com.spring.forex.repository.RateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class Analitic {
    @Autowired
    private RateRepository rateRepository;

    public List<List<String>> getStatistic(Currency currency, Period period) {
        List<Rate> currList = rateRepository.getByCurrencyIdPeriodId(currency.getId(), period.getId());

        double[] fromMinToMax = new double[24];
        double[] volume = new double[24];
        double[] openClose = new double[24];
        int[] pointMinMax = new int[24];

        int daysCount = 0;
        int prevDateOfYear = 0;

        double dayMax = 0;
        double dayMin = 1000000;
        int hourMax = -1;
        int hourMin = -1;

        for (Rate rate : currList) {
            int pos = rate.getDateTime().getHour() - 8;
            if (pos < 0) {
                pos += 24;
            }
            fromMinToMax[pos] += rate.getMax() - rate.getMin();
            openClose[pos] += Math.abs(rate.getOpen() - rate.getClose());
            volume[pos] += rate.getVolume();

            if (rate.getMax() > dayMax) {
                dayMax = rate.getMax();
                hourMax = pos;
            }
            if (rate.getMin() < dayMin) {
                dayMin = rate.getMin();
                hourMin = pos;
            }

            if (prevDateOfYear != rate.getDateTime().getDayOfYear()) {
                daysCount++;
                prevDateOfYear = rate.getDateTime().getDayOfYear();

                pointMinMax[hourMax] += 1;
                pointMinMax[hourMin] += 1;
                dayMax = 0;
                dayMin = 1000000;
                hourMax = hourMin = -1;
            }
        }
        if (hourMin > 0 && hourMax > 0) {
            pointMinMax[hourMax] += 1;
            pointMinMax[hourMin] += 1;
        }

        for (int i = 0; i < 24; i++) {
            fromMinToMax[i] = Math.ceil(fromMinToMax[i] * currency.getPointRatio() / daysCount);
            openClose[i] = Math.ceil(openClose[i] * currency.getPointRatio() / daysCount);
            volume[i] = Math.ceil(volume[i] / daysCount);
        }

        double fromMinToMaxMidle = Arrays.stream(fromMinToMax).average().getAsDouble();
        double openCloseMidle = Arrays.stream(openClose).average().getAsDouble();
        double volumeMidle = Arrays.stream(volume).average().getAsDouble();
        double minMidle = Arrays.stream(pointMinMax).average().getAsDouble();

        List<String> resMinMaxArr = new ArrayList<>();
        resMinMaxArr.add("min-max");
        resMinMaxArr.add(" " + (int) Math.ceil(fromMinToMaxMidle));

        List<String> resOpenClose = new ArrayList<>();
        resOpenClose.add("open-close");
        resOpenClose.add(" " + (int) Math.ceil(openCloseMidle));

        List<String> resVolume = new ArrayList<>();
        resVolume.add("volume");
        resVolume.add(" " + (int) Math.ceil(volumeMidle));

        List<String> resPointMinMax = new ArrayList<>();
        resPointMinMax.add("point min,max");
        resPointMinMax.add(" " + (int) Math.ceil(minMidle));

        for (int i = 0; i < 24; i++) {
            resMinMaxArr.add((fromMinToMax[i] / fromMinToMaxMidle >= 1.2) ? " " + (int) Math.ceil(fromMinToMax[i]) + " " : "");
            resOpenClose.add((openClose[i] / openCloseMidle >= 1.2) ? " " + (int) Math.ceil(openClose[i]) + " " : "");
            resVolume.add((volume[i] / volumeMidle >= 1.2) ? " " + (int) Math.ceil(volume[i]) + " " : "");
            resPointMinMax.add((pointMinMax[i] / minMidle >= 1.2) ? " " + (int) Math.ceil(pointMinMax[i]) + " " : "");
        }

        List<List<String>> result = new ArrayList<>();
        result.add(resMinMaxArr);
        result.add(resOpenClose);
        result.add(resPointMinMax);
        result.add(resVolume);

        return result;
    }

    public List<List<String>> openClosePair(Currency currency, Period period) {

        List<Rate> currList = rateRepository.getByCurrencyIdPeriodId(currency.getId(), period.getId());

        int daysCount = 0;
        int prevDateOfYear = 0;

        double dayMax = 0;
        double dayMin = 1000000;
        int hourMax = -1;
        int hourMin = -1;

        Map<String, Integer> map = new HashMap<>();

        for (Rate rate : currList) {
            int pos = rate.getDateTime().getHour() - 8;
            if (pos < 0) {
                pos += 24;
            }

            if (rate.getMax() > dayMax) {
                dayMax = rate.getMax();
                hourMax = pos;
            }
            if (rate.getMin() < dayMin) {
                dayMin = rate.getMin();
                hourMin = pos;
            }

            if (prevDateOfYear != rate.getDateTime().getDayOfYear()) {
                daysCount++;
                prevDateOfYear = rate.getDateTime().getDayOfYear();

                String key = (hourMin < hourMax) ? hourMin + "-" + hourMax : hourMax + "-" + hourMin;
                map.put(key, map.containsKey(key) ? map.get(key) + 1 : 1);

                dayMax = 0;
                dayMin = 1000000;
                hourMax = hourMin = -1;
            }
        }
        if (hourMin > 0 && hourMax > 0) {
            String key = (hourMin < hourMax) ? hourMin + "-" + hourMax : hourMax + "-" + hourMin;
            map.put(key, map.containsKey(key) ? map.get(key) + 1 : 1);
        }

        System.out.println(currency);
        for (String key : map.keySet()) {
            System.out.println(map.get(key) + ";" + key);
        }

        return null;
    }
}
