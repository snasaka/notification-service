package com.noteif.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.noteif.repository.TransactionRepository;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @RequestMapping(value = "/data-points/{applicationId}")
    public Map<String, Long> getCountByApplication(@PathVariable UUID applicationId,
                                        @RequestParam int timeline) {

        Map<String, Long> dataPoints = new TreeMap();

        Map<String, Long> countByDate = transactionRepository.findByApplicationIdAndDateCreatedAfter(applicationId, DateUtils.addMinutes(new Date(),-timeline))
                .stream()
                .map(transaction -> {
                    Date time = transaction.getDateCreated();
                    Instant instant = Instant.ofEpochMilli(time.getTime());
                    return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm"));
                })
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));


        LocalTime timeNow = LocalTime.now();
        for (LocalTime time = timeNow; time.isAfter(timeNow.minusMinutes(timeline)); time = time.minusMinutes(1)) {
            String formattedTime = time.format(DateTimeFormatter.ofPattern("hh:mm"));
            dataPoints.put(formattedTime, countByDate.get(formattedTime) == null ? 0 : countByDate.get(formattedTime));
        }

        return dataPoints;
    }
}