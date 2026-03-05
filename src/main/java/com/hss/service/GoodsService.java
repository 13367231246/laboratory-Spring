package com.hss.service;

import com.hss.pojo.Goods;

import java.util.List;

public interface GoodsService {
    void addGoods(Goods goods);

    void updateGoods(Goods goods);

    List<Goods> getGoodsById();
}
