package com.imooc.flashsale.service;

import java.util.List;

import com.imooc.flashsale.vo.GoodsVo;

public interface IGoodsService {

	/**
	 * 查询商品列表
	 * 
	 * @return
	 */
	public List<GoodsVo> getGoodsVoList();

	/**
	 * 获得商品详情信息
	 * 
	 * @param goodsId
	 * @return
	 */
	public GoodsVo getGoodsVoById(long goodsId);

	/**
	 * 减少库存
	 */
	public boolean reduceStock(GoodsVo goodsVo);

}
