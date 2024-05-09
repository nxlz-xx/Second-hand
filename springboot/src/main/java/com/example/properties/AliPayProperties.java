package com.example.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "alipay")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AliPayProperties {

    private String appId;
    private String appPrivateKey;
    private String alipayPublicKey;
    private String notifyUrl;

}
