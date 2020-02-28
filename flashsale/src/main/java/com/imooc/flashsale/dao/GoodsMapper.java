package com.imooc.flashsale.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.imooc.flashsale.domain.Goods;
import com.imooc.flashsale.domain.GoodsExample;
import com.imooc.flashsale.vo.GoodsVo;

@Mapper
public interface GoodsMapper {
	long countByExample(GoodsExample example);

	int deleteByExample(GoodsExample example);

	int deleteByPrimaryKey(Long id);

	int insert(Goods record);

	int insertSelective(Goods record);

	List<Goods> selectByExampleWithBLOBs(GoodsExample example);

	List<Goods> selectByExample(GoodsExample example);

	Goods selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") Goods record, @Param("example") GoodsExample example);

	int updateByExampleWithBLOBs(@Param("record") Goods record, @Param("example") GoodsExample example);

	int updateByExample(@Param("record") Goods record, @Param("example") GoodsExample example);

	int updateByPrimaryKeySelective(Goods record);

	int updateByPrimaryKeyWithBLOBs(Goods record);

	int updateByPrimaryKey(Goods record);

	@Select("SELECT g.*, fg.flashsale_price,fg.stock_count,fg.start_date,fg.end_date FROM tb_flashsale_goods fg LEFT JOIN tb_goods g ON fg.goods_id = g.id")
	List<GoodsVo> selectGoodsVoList();

	@Select("SELECT g.*, fg.flashsale_price,fg.stock_count,fg.start_date,fg.end_date FROM tb_flashsale_goods fg LEFT JOIN tb_goods g ON fg.goods_id = g.id WHERE g.id = #{goodsId}")
	GoodsVo selectGoodsVoById(@Param("goodsId") long goodsId);
}