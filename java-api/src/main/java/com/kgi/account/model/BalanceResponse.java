package com.kgi.account.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 帳戶餘額查詢回應 DTO
 * 
 * 用於回傳帳戶餘額查詢的結果，包含帳號、餘額和幣別
 * 
 * @author Bob AI
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceResponse {
    
    /**
     * 帳號
     */
    private String accountId;
    
    /**
     * 帳戶餘額
     */
    private BigDecimal balance;
    
    /**
     * 幣別（固定為 TWD）
     */
    private String currency;
}

// Made with Bob
