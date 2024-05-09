package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 订单信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
    /** ID */
    private Integer id;
    /** 商品ID */
    private Integer goodsId;
    /** 商品名称 */
    private String goodsName;
    /** 商品图片 */
    private String goodsImg;
    /** 订单编号 */
    private String orderNo;
    /** 总价 */
    private BigDecimal total;
    /** 下单时间 */
    private String time;
    /** 支付单号 */
    private String payNo;
    /** 支付时间 */
    private String payTime;
    /** 下单人ID */
    private Integer userId;
    /** 收货地址 */
    private String address;
    /** 联系方式 */
    private String phone;
    /** 收货人名称 */
    private String userName;
    /** 订单状态 */
    private String status;
    /** 卖家ID */
    private Integer saleId;

    private Integer addressId;
    private String saleName;


}