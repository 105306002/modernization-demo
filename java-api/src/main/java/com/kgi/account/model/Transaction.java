package com.kgi.account.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易記錄資料模型
 * 
 * 用於表示帳戶的交易記錄，包含交易日期、金額、描述和交易後餘額
 * 
 * @author Bob AI
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    
    /**
     * 交易日期時間
     */
    private LocalDateTime date;
    
    /**
     * 交易金額（正數為入帳，負數為支出）
     */
    private BigDecimal amount;
    
    /**
     * 交易描述
     */
    private String description;
    
    /**
     * 交易後餘額
     */
    private BigDecimal balance;
}

// Made with Bob
