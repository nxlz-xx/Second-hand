package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 聊天组
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatGroup {

    /** ID */
    private Integer id;
    /** 聊天用户ID */
    private Integer chatUserId;
    /** 当前用户ID */
    private Integer userId;

    private String chatUserName;
    private String chatUserAvatar;
    private Integer chatNum;

}