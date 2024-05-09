package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 二手商品
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Goods implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;
    /** 名称 */
    private String name;
    /** 价格 */
    private BigDecimal price;
    /** 详情 */
    private String content;
    /** 发货地址 */
    private String address;
    /** 图片 */
    private String img;
    /** 上架日期 */
    private String date;
    /** 审核状态 */
    private String status;
    /** 分类 */
    private String category;
    /** 所属用户ID */
    private Integer userId;
    /** 上架状态 */
    private String saleStatus;
    /** 浏览量 */
    private Integer readCount;
    /** 点赞量 */
    private Integer likesCount;
    /** 收藏量 */
    private Integer collectCount;

    private String userName;
    private String sort;
    private Boolean userLikes;
    private Boolean userCollect;


}