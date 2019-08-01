package com.millward.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock_symbol")
    private String stockSymbol;
    private String name;
    private String website;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    private Set<Price> priceUpdate = new HashSet<>();

    public Company() { }

    public Company(String stockSymbol, String name, String website, Set<Price> priceUpdate) {
        this.stockSymbol = stockSymbol;
        this.name = name;
        this.website = website;
        this.priceUpdate = priceUpdate;
    }

    public Company(Long id, String stockSymbol, String name, String website, Set<Price> priceUpdate) {
        this.id = id;
        this.stockSymbol = stockSymbol;
        this.name = name;
        this.website = website;
        this.priceUpdate = priceUpdate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Set<Price> getPriceUpdate() {
        return priceUpdate;
    }

    public void setPriceUpdate(Set<Price> priceUpdate) {
        this.priceUpdate = priceUpdate;
    }
}
