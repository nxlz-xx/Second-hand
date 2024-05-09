package com.example.controller;

import com.example.exception.CustomException;
import com.example.properties.ValidateCodeProperties;
import com.example.utils.ValidateCodeCache;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class CaptchaController {

    @GetMapping("/validateCode")
    public void getValidateCode(HttpServletRequest request, HttpServletResponse response) {
        // 生成验证码
        ValidateCodeProperties code = new ValidateCodeProperties();
        setHeader(response, code.getType());
        Captcha captcha = createCaptcha(code);
        // 存储验证码到缓存
        ValidateCodeCache.setCache(request.getParameter("key"), captcha.text().toLowerCase());
        try {
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("50011", "验证码生成失败");
        }
    }

    /**
     * 创建验证码
     */
    private Captcha createCaptcha(ValidateCodeProperties code) {
        Captcha captcha = null;
        if ("gif".equalsIgnoreCase(code.getType())) {
            captcha = new GifCaptcha(code.getWidth(), code.getHeight(), code.getLength());
        } else {
            captcha = new SpecCaptcha(code.getWidth(), code.getHeight(), code.getLength());
        }
        captcha.setCharType(code.getCharType());

        return captcha;
    }

    /**
     * 设置验证码返回头
     */
    private void setHeader(HttpServletResponse response, String type) {
        if ("gif".equalsIgnoreCase(type)) {
            response.setContentType(MediaType.IMAGE_GIF_VALUE);
        } else {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
        }
        response.setHeader(HttpHeaders.PRAGMA, "No-cache");
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
        response.setDateHeader(HttpHeaders.EXPIRES, 0L);
    }

}
