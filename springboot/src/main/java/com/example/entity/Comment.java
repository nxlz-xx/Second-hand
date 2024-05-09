package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 评论表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;
    /** 内容 */
    private String content;
    /** 评论人 */
    private Integer userId;
    /** 父级ID */
    private Integer pid;
    /** 评论时间 */
    private String time;
    /** 关联ID */
    private Integer fid;
    /** 模块 */
    private String module;
    /** 根节点ID */
    private Integer rootId;

    private List<Comment> children;
    private String userName;
    private String userAvatar;
    private String parentName;


}