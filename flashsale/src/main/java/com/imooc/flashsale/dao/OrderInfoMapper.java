package com.imooc.flashsale.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.imooc.flashsale.domain.OrderInfo;
import com.imooc.flashsale.domain.OrderInfoExample;

@Mapper
public interface OrderInfoMapper {
	long countByExample(OrderInfoExample example);

	int deleteByExample(OrderInfoExample example);

	int deleteByPrimaryKey(Long id);

	int insert(OrderInfo record);

	int insertSelective(OrderInfo record);

	List<OrderInfo> selectByExample(OrderInfoExample example);

	OrderInfo selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") OrderInfo record, @Param("example") OrderInfoExample example);

	int updateByExample(@Param("record") OrderInfo record, @Param("example") OrderInfoExample example);

	int updateByPrimaryKeySelective(OrderInfo record);

	int updateByPrimaryKey(OrderInfo record);
}