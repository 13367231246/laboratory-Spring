package com.hss.service.impl;

import com.hss.mapper.GoodsMapper;
import com.hss.pojo.Goods;
import com.hss.pojo.Result;
import com.hss.service.GoodsService;
import com.hss.utils.AliOssUtil;
import com.hss.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;

    // 添加商品
    @Override
    public void addGoods(Goods goods) {
        goods.setUpdateTime(LocalDateTime.now());
        goods.setCreateTime(LocalDateTime.now());
        //从TheadLocal中获取用户id
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        goods.setCreateUser(id);
        goodsMapper.addGoods(goods);
    }

    // 更新商品
    @Override
    public void updateGoods(Goods goods) {
        goods.setUpdateTime(LocalDateTime.now());
        goodsMapper.updateGoods(goods);
    }

    @Override
    public List<Goods> getGoodsById() {
         return goodsMapper.getGoodsById();
    }
}
