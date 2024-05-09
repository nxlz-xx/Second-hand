package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 点赞
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Likes {

    private Integer id;
    private Integer goodsId;
    private Integer userId;
    private String module;
}
