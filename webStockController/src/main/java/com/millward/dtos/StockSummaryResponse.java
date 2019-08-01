package com.millward.dtos;

import java.util.Date;
import java.util.List;

import com.millward.dtos.StockSummary;

//Returns stock summary data requested by client with a time marker for delta updates

public class StockSummaryResponse {
	private Date serverUpdatedTime;
	private List<StockSummary> data;

	public StockSummaryResponse(Date serverUpdatedTime, List<StockSummary> data) {
		this.serverUpdatedTime = serverUpdatedTime;
		this.data = data;
	}

	public Date getServerUpdatedTime() {
		return serverUpdatedTime;
	}

	public void setServerUpdatedTime(Date serverUpdatedTime) {
		this.serverUpdatedTime = serverUpdatedTime;
	}

	public List<StockSummary> getData() {
		return data;
	}

	public void setData(List<StockSummary> data) {
		this.data = data;
	}
}
