package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色用户父类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private Integer id;
    /** 用户名 */
    private String username;
    /** 名称 */
    private String name;
    /** 密码 */
    private String password;
    /** 角色标识 */
    private String role;
    /** 新密码 */
    private String newPassword;
    /** 头像 */
    private String avatar;

    private String token;
    private String key;
    private String code;

}
