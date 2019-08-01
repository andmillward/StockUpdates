package com.millward.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import com.millward.dtos.StockSummary;
import com.millward.entities.Company;
import com.millward.entities.Price;
import com.millward.dtos.StockSummaryResponse;
import com.millward.repositories.CompanyRepository;
import com.millward.repositories.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class StockService {

	private Map<Long, Date> stockUpdateTimeCache;

	private Date lastCacheUpdateTime;

	private final PriceRepository priceRepository;
	private final CompanyRepository companyRepository;
	private final StockDataService stockDataService;

	@Autowired
	public StockService(PriceRepository priceRepository, CompanyRepository companyRepository, StockDataService stockDataService){
		this.priceRepository = priceRepository;
		this.companyRepository = companyRepository;
		this.stockDataService = stockDataService;

		initializeCache(companyRepository);
	}

	// Create a cache for when each company has been updated
	private void initializeCache(CompanyRepository companyRepository) {
		Map<Long, Date> workingCache = new HashMap<>();
		Date now = new Date();
		companyRepository.findAll().forEach(company -> workingCache.put(company.getId(), now));
		this.stockUpdateTimeCache = Collections.unmodifiableMap(workingCache);
		this.lastCacheUpdateTime = now;
	}

	// Need to write latest data from StocksDataService and overwrite cache
	@Scheduled(fixedDelay = 15000)
	public void updatePrices() {
		List<Price> priceUpdates = new ArrayList<>();
		Date now = new Date();
		Map<Long, Date> workingCache = new HashMap<>(this.stockUpdateTimeCache);

		// Build each Price entity for return
		for (Map.Entry<Long, Double> priceUpdate : this.stockDataService.getData().entrySet()) {
			Price newPrice = new Price();
			Company company = companyRepository.findById(priceUpdate.getKey());
			newPrice.setPrice(BigDecimal.valueOf(priceUpdate.getValue()));
			newPrice.setTransactionTime(new Date());
			newPrice.setCompany(company);
			priceUpdates.add(newPrice);
		}

		priceRepository.save(priceUpdates);
		priceUpdates.forEach(price -> workingCache.put(price.getCompany().getId(), now));
		this.stockUpdateTimeCache = Collections.unmodifiableMap(workingCache);
		this.lastCacheUpdateTime = now;
	}

	// Client wants every summary
	public StockSummaryResponse getStockSummaries(){
		Date currentCacheUpdateTime = this.lastCacheUpdateTime;
		return new StockSummaryResponse(currentCacheUpdateTime, this.createStockSummaries());
	}

	// Client wants only the summary updates since given time.
	public StockSummaryResponse getStockSummaryUpdates(Date timeOfClientUpdate) {
		Date currentCacheUpdateTime = this.lastCacheUpdateTime;
		Map<Long, Date> currentCache = this.stockUpdateTimeCache;
		Set<Long> updatedCompanies = new HashSet<>();

		if (currentCacheUpdateTime == timeOfClientUpdate) {
			return new StockSummaryResponse(currentCacheUpdateTime, new ArrayList<>());
		}

		// Get companies updated since updateTime and set the last cache update time for response to client
		for (Map.Entry<Long, Date> entry : currentCache.entrySet()) {
			if (entry.getValue().after(timeOfClientUpdate)) {
				updatedCompanies.add(entry.getKey());
			}
		}

		return new StockSummaryResponse(currentCacheUpdateTime, getStockSummariesForCompanies(updatedCompanies));
	}

	// List of companies with updates found, get just them from all stock summaries
	private List<StockSummary> getStockSummariesForCompanies(Set<Long> updatedCompanies) {
		return this.createStockSummaries().stream()
				.filter(stockSummary -> updatedCompanies.contains(stockSummary.getCompany_id()))
				.collect(Collectors.toList());
	}

	private List<Price> getClosingPrices() {
		LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
		Date midnight = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		return priceRepository.findLatestBefore(midnight);
	}

	private List<Price> getLatestPrices() {
		return priceRepository.findLatest();
	}

	private List<Company> getCompanies() {
		return companyRepository.findAll();
	}

	// Need a summary of current stocks for every company
	private List<StockSummary> createStockSummaries() {
		List<Company> companies = getCompanies();
		List<Price> closingPrices = getClosingPrices();
		List<Price> latestPrices = getLatestPrices();
		List<StockSummary> summaries = new ArrayList<>();

		// Build summaries for return
		for (Company company : companies) {
			Price currentPrice = getPriceForCompany(latestPrices, company.getId());
			Price closingPrice = getPriceForCompany(closingPrices, company.getId());

			if (closingPrice == null) {
				closingPrice = priceRepository.findTop1ByCompanyIdOrderByTransactionTime(company.getId());
			}
			if (closingPrice != null && currentPrice != null) {
				BigDecimal change = currentPrice.getPrice().subtract(closingPrice.getPrice());
				BigDecimal percent = change.divide(closingPrice.getPrice(), 6, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
				StockSummary stockSummary = new StockSummary(
						company.getId(),
						company.getStockSymbol(),
						company.getName(),
						company.getWebsite(),
						currentPrice.getPrice(), change,
						percent.setScale(2, RoundingMode.HALF_UP)
				);
				summaries.add(stockSummary);
			}
		}
		return summaries;
	}

	// Need to find the price in a list for a given company
	private Price getPriceForCompany(List<Price> prices, Long companyId) {
		for (Price price : prices) {
			if (price.getCompany().getId().equals(companyId)) {
				return price;
			}
		}
		return null;
	}
}
