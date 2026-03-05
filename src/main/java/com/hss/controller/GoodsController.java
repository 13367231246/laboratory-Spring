package com.hss.controller;

import com.hss.pojo.Goods;
import com.hss.pojo.Result;
import com.hss.service.GoodsService;
import com.hss.utils.AliOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    // 新增商品
    @PostMapping("/add")
    public Result addGoods(@RequestBody Goods goods) {
        goodsService.addGoods(goods);
        return Result.success();
    }
    // 修改商品
    @PostMapping("/update")
    public Result updateGoods(@RequestBody Goods goods) {
        goodsService.updateGoods(goods);
        return Result.success();
    }
    // 获取商品信息
    @GetMapping("/get")
    public Result<List<Goods>> getGoods() {
        List<Goods> goods = goodsService.getGoodsById();
        return Result.success(goods);
    }

}
