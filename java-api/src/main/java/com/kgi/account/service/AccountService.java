package com.kgi.account.service;

import com.kgi.account.model.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 帳戶服務類別
 * 
 * 提供帳戶餘額查詢和交易記錄查詢的業務邏輯
 * 使用記憶體內的 Map 模擬帳戶資料
 * 
 * @author Bob AI
 * @version 1.0
 */
@Service
public class AccountService {
    
    /**
     * 模擬銀行的帳戶資料
     * 使用靜態 Map 儲存帳號和餘額的對應關係
     */
    private static final Map<String, BigDecimal> accounts = new HashMap<>();
    
    static {
        // 初始化測試資料
        accounts.put("ACC001", new BigDecimal("50000.00"));   // 張先生的帳戶
        accounts.put("ACC002", new BigDecimal("120000.00"));  // 李小姐的帳戶
        accounts.put("ACC003", new BigDecimal("8500.50"));    // 王先生的帳戶
    }
    
    /**
     * 查詢帳戶餘額
     * 
     * @param accountId 帳號
     * @return 帳戶餘額，若帳號不存在則回傳 0
     */
    public BigDecimal getBalance(String accountId) {
        return accounts.getOrDefault(accountId, BigDecimal.ZERO);
    }
    
    /**
     * 檢查帳戶是否存在
     * 
     * @param accountId 帳號
     * @return 若帳戶存在回傳 true，否則回傳 false
     */
    public boolean accountExists(String accountId) {
        return accounts.containsKey(accountId);
    }
    
    /**
     * 查詢近期交易記錄
     * 
     * 回傳模擬的交易記錄（3 筆），按時間倒序排列（最新在前）
     * 
     * @param accountId 帳號
     * @return 交易記錄列表
     */
    public List<Transaction> getRecentTransactions(String accountId) {
        BigDecimal currentBalance = getBalance(accountId);
        List<Transaction> transactions = new ArrayList<>();
        
        // 第一筆交易：ATM 提款（最新）
        transactions.add(new Transaction(
            LocalDateTime.now().minusDays(1),
            new BigDecimal("-500"),
            "ATM 提款",
            currentBalance  // 交易後餘額：50000
        ));
        
        // 第二筆交易：薪資入帳
        transactions.add(new Transaction(
            LocalDateTime.now().minusDays(2),
            new BigDecimal("3000"),
            "薪資入帳",
            currentBalance.add(new BigDecimal("500"))  // 交易後餘額：50500
        ));
        
        // 第三筆交易：信用卡繳費（最舊）
        transactions.add(new Transaction(
            LocalDateTime.now().minusDays(5),
            new BigDecimal("-1200"),
            "信用卡繳費",
            currentBalance.add(new BigDecimal("500")).subtract(new BigDecimal("3000"))  // 交易後餘額：47500
        ));
        
        return transactions;
    }
}

// Made with Bob
