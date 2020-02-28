package com.imooc.flashsale.vo;

import java.util.Date;

import com.imooc.flashsale.domain.Goods;

public class GoodsVo extends Goods {

	private Double flashsalePrice;

	private Integer stockCount;

	private Date startDate;

	private Date endDate;

	public Double getFlashsalePrice() {
		return flashsalePrice;
	}

	public void setFlashsalePrice(Double flashsalePrice) {
		this.flashsalePrice = flashsalePrice;
	}

	public Integer getStockCount() {
		return stockCount;
	}

	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "GoodsVo [flashsalePrice=" + flashsalePrice + ", stockCount=" + stockCount + ", startDate=" + startDate
				+ ", endDate=" + endDate + "]";
	}

}
