package com.kgi.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 凱基銀行帳戶查詢 API 應用程式進入點
 * 
 * 此應用程式提供帳戶餘額查詢和交易記錄查詢功能
 * 從 .NET Core 遷移到 Java/Spring Boot
 * 
 * @author Bob AI
 * @version 1.0
 */
@SpringBootApplication
public class AccountApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class, args);
        System.out.println("===========================================");
        System.out.println("凱基銀行帳戶查詢 API (Java/Spring Boot) 已啟動");
        System.out.println("API 端點：http://localhost:8080");
        System.out.println("測試範例：");
        System.out.println("  curl http://localhost:8080/api/account/ACC001/balance");
        System.out.println("  curl http://localhost:8080/api/account/ACC001/transactions");
        System.out.println("===========================================");
    }
}

// Made with Bob
