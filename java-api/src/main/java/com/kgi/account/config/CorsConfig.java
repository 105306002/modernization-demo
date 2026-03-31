package com.kgi.account.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS 跨域資源共享配置
 * 
 * 允許前端應用程式從不同來源存取 API
 * 
 * @author Bob AI
 * @version 1.0
 */
@Configuration
public class CorsConfig {
    
    /**
     * 配置 CORS 規則
     * 
     * @return WebMvcConfigurer 實例
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")      // 允許所有來源（開發環境）
                        .allowedMethods("*")      // 允許所有 HTTP 方法
                        .allowedHeaders("*");     // 允許所有標頭
            }
        };
    }
}

// Made with Bob
