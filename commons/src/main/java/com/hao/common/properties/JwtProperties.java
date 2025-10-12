package com.hao.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "hao.jwt")
@Data
public class JwtProperties {

    private String userSecretKey;
    private long userTtl;
    private String userTokenName;

    // 开放接口
    private List<String> publicPaths = new ArrayList<>();
    // 登录注册专用接口
    private List<String> authPaths = new ArrayList<>();
    // 师生共享接口
    private List<String> sharedPaths = new ArrayList<>();
    //
    private Map<String, List<String>> roleBasedPath;
}
