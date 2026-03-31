package com.kgi.account.exception;

/**
 * 帳戶不存在例外
 * 
 * 當查詢的帳號不存在時拋出此例外
 * 
 * @author Bob AI
 * @version 1.0
 */
public class AccountNotFoundException extends RuntimeException {
    
    private final String accountId;
    
    /**
     * 建構子
     * 
     * @param accountId 不存在的帳號
     */
    public AccountNotFoundException(String accountId) {
        super("帳戶不存在: " + accountId);
        this.accountId = accountId;
    }
    
    /**
     * 取得帳號
     * 
     * @return 帳號
     */
    public String getAccountId() {
        return accountId;
    }
}

// Made with Bob
