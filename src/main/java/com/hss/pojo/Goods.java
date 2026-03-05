package com.hss.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Goods {
    private Integer id;
    private String categoryId;
    private String goodsName;
    private Double price;
    private String description;
    private Integer stock;//库存
    private Integer sales;//销量
    private String image;

    private Integer createUser;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
