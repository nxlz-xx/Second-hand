package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 收藏
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Collect {

    private Integer id;
    private Integer goodsId;
    private Integer userId;
    private String module;
    private String createTime;

    private String goodsName;
    private String goodsImg;
}
