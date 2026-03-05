package com.hss.mapper;

import com.hss.pojo.contactUs;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ContactMapper {
    @Insert("insert into contact_us(address_image, wechat_code, qq_code," +
            " company_address, pre_sales, after_sales, market_coop) " +
            "values(#{addressImage}, #{wechatCode}, #{qqCode}, #{companyAddress}, #{preSales}, #{afterSales}, #{marketCoop})")
    void add(contactUs contact);

    @Select("select * from contact_us")
    contactUs query();
}
