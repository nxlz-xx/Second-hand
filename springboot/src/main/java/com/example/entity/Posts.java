package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 帖子
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Posts implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;
    /** 标题 */
    private String title;
    /** 内容 */
    private String content;
    /** 图片 */
    private String img;
    /** 用户ID */
    private Integer userId;
    /** 发布时间 */
    private String time;
    /** 圈子 */
    private String circle;
    /** 简介 */
    private String descr;
    /** 浏览量 */
    private Integer readCount;
    /** 状态 */
    private String status;

    private String userName;

}