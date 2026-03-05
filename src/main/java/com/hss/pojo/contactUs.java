package com.hss.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class contactUs {

    private Integer id; // 唯一标识
    private String addressImage; // 公司地址图片路径
    private String wechatCode; // 微信二维码图片路径
    private String qqCode; // QQ二维码图片路径
    private String companyAddress; // 公司地址文字信息
    private String preSales; // 售前咨询信息
    private String afterSales; // 售后热线信息
    private String marketCoop; // 市场合作信息
}
