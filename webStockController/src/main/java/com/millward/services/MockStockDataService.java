package com.millward.services;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

@Service
public class MockStockDataService implements StockDataService{

	//Sample data to manipulate mimicking real data
	private Map<Long, Double> mockData = new HashMap<Long, Double>() {{
		this.put(1L, 324.32);
		this.put(2L, 234.45);
		this.put(3L, 89.23);
		this.put(4L, 385.67);
		this.put(5L, 387.88);
		this.put(6L, 267.24);
		this.put(7L, 346.39);
		this.put(8L, 489.34);
		this.put(9L, 27.90);
		this.put(10L, 342.09);
		this.put(11L, 382.09);
		this.put(12L, 2080.11);
	}};

	//Returns data randomized within bounds
	public Map<Long, Double> getData() {
		Map<Long, Double> updates = new HashMap<>();
		for (Map.Entry<Long, Double> price : mockData.entrySet()) {
			if (ThreadLocalRandom.current().nextInt(5) == 0 && price.getKey() != 7L) {
				price.setValue(price.getValue() + ThreadLocalRandom.current().nextDouble(-8, 8.1));
				updates.put(price.getKey(), price.getValue());
			}
		}
		return updates;
	}
}
