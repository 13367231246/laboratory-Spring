package com.hss.mapper;

import com.hss.pojo.Goods;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsMapper {

//    添加商品
@Insert("INSERT INTO goods (category_id, goodsName, price, description," +
        " stock, sales, image, create_user, create_time, update_time) " +
        "values (#{categoryId}, #{goodsName}, #{price}, #{description}, #{stock}, #{sales}, #{image}, #{createUser}, #{createTime}, #{updateTime})")
    void addGoods(Goods goods);

//    更新商品
    @Insert("UPDATE goods SET category_id = #{categoryId}, goodsName = #{goodsName}, price = #{price}, description = #{description}, " +
            "stock = #{stock}, sales = #{sales}, image = #{image}, update_time = #{updateTime} WHERE category_id = #{categoryId}")
    void updateGoods(Goods goods);

    //    根据id查询商品
    @Select("SELECT * FROM goods")
    List<Goods> getGoodsById();
}
