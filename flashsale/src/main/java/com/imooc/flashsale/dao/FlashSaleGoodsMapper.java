package com.imooc.flashsale.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.imooc.flashsale.domain.FlashSaleGoods;
import com.imooc.flashsale.domain.FlashSaleGoodsExample;

@Mapper
public interface FlashSaleGoodsMapper {
	long countByExample(FlashSaleGoodsExample example);

	int deleteByExample(FlashSaleGoodsExample example);

	int deleteByPrimaryKey(Long id);

	int insert(FlashSaleGoods record);

	int insertSelective(FlashSaleGoods record);

	List<FlashSaleGoods> selectByExample(FlashSaleGoodsExample example);

	FlashSaleGoods selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") FlashSaleGoods record,
			@Param("example") FlashSaleGoodsExample example);

	int updateByExample(@Param("record") FlashSaleGoods record, @Param("example") FlashSaleGoodsExample example);

	int updateByPrimaryKeySelective(FlashSaleGoods record);

	int updateByPrimaryKey(FlashSaleGoods record);
}