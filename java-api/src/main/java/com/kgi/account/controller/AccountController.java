package com.kgi.account.controller;

import com.kgi.account.model.BalanceResponse;
import com.kgi.account.model.Transaction;
import com.kgi.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 帳戶 REST 控制器
 * 
 * 提供帳戶餘額查詢和交易記錄查詢的 API 端點
 * 
 * @author Bob AI
 * @version 1.0
 */
@RestController
@RequestMapping("/api/account")
public class AccountController {
    
    private final AccountService service;
    
    /**
     * 建構子注入 AccountService
     * 
     * @param service 帳戶服務
     */
    @Autowired
    public AccountController(AccountService service) {
        this.service = service;
    }
    
    /**
     * 查詢帳戶餘額
     * 
     * @param accountId 帳號
     * @return 包含帳號、餘額和幣別的回應，若帳號不存在則回傳 404
     */
    @GetMapping("/{accountId}/balance")
    public ResponseEntity<?> getBalance(@PathVariable String accountId) {
        BigDecimal balance = service.getBalance(accountId);
        
        // 檢查帳戶是否存在（餘額為 0 且帳戶不存在）
        if (balance.compareTo(BigDecimal.ZERO) == 0 && !service.accountExists(accountId)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "帳戶不存在");
            error.put("accountId", accountId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        
        // 建立回應物件
        BalanceResponse response = new BalanceResponse(accountId, balance, "TWD");
        return ResponseEntity.ok(response);
    }
    
    /**
     * 查詢交易記錄
     * 
     * @param accountId 帳號
     * @return 交易記錄列表，若帳號不存在則回傳 404
     */
    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<?> getTransactions(@PathVariable String accountId) {
        // 檢查帳戶是否存在
        if (!service.accountExists(accountId)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "帳戶不存在");
            error.put("accountId", accountId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        
        // 查詢交易記錄
        List<Transaction> transactions = service.getRecentTransactions(accountId);
        return ResponseEntity.ok(transactions);
    }
}

// Made with Bob
