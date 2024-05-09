package com.example.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 验证码缓存
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeCache {
    private String key;
    private String code;
    private long timestamp;


}
