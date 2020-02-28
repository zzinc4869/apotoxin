package com.imooc.flashsale.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.imooc.flashsale.domain.FlashSaleOrder;
import com.imooc.flashsale.domain.FlashSaleOrderExample;

@Mapper
public interface FlashSaleOrderMapper {
	long countByExample(FlashSaleOrderExample example);

	int deleteByExample(FlashSaleOrderExample example);

	int deleteByPrimaryKey(Long id);

	int insert(FlashSaleOrder record);

	int insertSelective(FlashSaleOrder record);

	List<FlashSaleOrder> selectByExample(FlashSaleOrderExample example);

	FlashSaleOrder selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") FlashSaleOrder record,
			@Param("example") FlashSaleOrderExample example);

	int updateByExample(@Param("record") FlashSaleOrder record, @Param("example") FlashSaleOrderExample example);

	int updateByPrimaryKeySelective(FlashSaleOrder record);

	int updateByPrimaryKey(FlashSaleOrder record);

	@Select("SELECT * FROM tb_flashsale_order WHERE user_id = #{userId} AND goods_id = #{goodsId}")
	FlashSaleOrder selectByUserIdAndGoodsId(@Param("userId") Long userId, @Param("goodsId") Long goodsId);
}