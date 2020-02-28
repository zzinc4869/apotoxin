package com.imooc.flashsale.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imooc.flashsale.dao.FlashSaleGoodsMapper;
import com.imooc.flashsale.dao.GoodsMapper;
import com.imooc.flashsale.domain.FlashSaleGoods;
import com.imooc.flashsale.domain.FlashSaleGoodsExample;
import com.imooc.flashsale.domain.FlashSaleGoodsExample.Criteria;
import com.imooc.flashsale.service.IGoodsService;
import com.imooc.flashsale.vo.GoodsVo;

@Service
public class GoodsServiceImpl implements IGoodsService {

	@Autowired
	private GoodsMapper goodsDao;

	@Autowired
	private FlashSaleGoodsMapper flashSaleGoodsDao;

	@Override
	public List<GoodsVo> getGoodsVoList() {
		List<GoodsVo> listGoods = goodsDao.selectGoodsVoList();
		// System.out.println("查询GoodsVolist=" + listGoods);
		return listGoods;
	}

	@Override
	public GoodsVo getGoodsVoById(long goodsId) {
		GoodsVo goodsVo = goodsDao.selectGoodsVoById(goodsId);
		// System.out.println("查询GoodsVo=" + goodsVo);
		return goodsVo;
	}

	@Override
	public boolean reduceStock(GoodsVo goodsVo) {
		// 更新库存
		FlashSaleGoods record = new FlashSaleGoods();
		record.setGoodsId(goodsVo.getId());
		record.setStockCount(goodsVo.getStockCount() - 1);
		// 创建Where条件
		FlashSaleGoodsExample example = new FlashSaleGoodsExample();
		Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(goodsVo.getId());
		criteria.andStockCountGreaterThan(0);

		System.out.println("更新库存：" + record);
		int effectedNums = flashSaleGoodsDao.updateByExampleSelective(record, example);

		return effectedNums > 0;
	}

}
