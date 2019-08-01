package com.millward.dtos;

import java.math.BigDecimal;

//Holds a summary of quick information about a single stock.

public class StockSummary {

    private Long company_id;
    private String stockSymbol;
    private String name;
    private String website;
    private BigDecimal price;
    private BigDecimal change;
    private BigDecimal percent;

    public StockSummary(Long company_id, String stockSymbol, String name, String website, BigDecimal price, BigDecimal change, BigDecimal percent) {
        this.company_id = company_id;
        this.stockSymbol = stockSymbol;
        this.name = name;
        this.website = website;
        this.price = price;
        this.change = change;
        this.percent = percent;
    }

    public Long getCompany_id(){
        return company_id;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public String getName() {
        return name;
    }

    public String getWebsite() {
        return website;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getChange() {
        return change;
    }

    public BigDecimal getPercent() {
        return percent;
    }
}
