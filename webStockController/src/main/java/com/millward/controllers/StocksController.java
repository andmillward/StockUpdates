package com.millward.controllers;

import java.util.Date;

import com.millward.dtos.StockSummaryResponse;
import com.millward.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StocksController {

    @Autowired
    StockService stockService;

    @GetMapping("/stock-summaries")
    public StockSummaryResponse getSummaries(
            @RequestParam(value = "update_time", required = false)
                    Long updateMilliseconds) {
        StockSummaryResponse summaryResponse;
        if(updateMilliseconds == null) {
            summaryResponse = stockService.getStockSummaries();
        } else{
            Date updateTime = new Date(updateMilliseconds);
            summaryResponse = stockService.getStockSummaryUpdates(updateTime);
        }
        return summaryResponse;
    }
}