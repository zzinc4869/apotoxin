package com.imooc.flashsale.domain;

import java.util.Date;

public class FlashSaleGoods {
	private Long id;

	private Long goodsId;

	private Double flashsalePrice;

	private Integer stockCount;

	private Date startDate;

	private Date endDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

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
		return "FlashSaleGoods [id=" + id + ", goodsId=" + goodsId + ", flashsalePrice=" + flashsalePrice
				+ ", stockCount=" + stockCount + ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}

}