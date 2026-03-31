package com.kgi.account.service;

import com.kgi.account.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AccountService 單元測試
 * 
 * 測試帳戶服務的業務邏輯，包括餘額查詢、帳戶存在檢查和交易記錄查詢
 * 
 * @author Bob AI
 * @version 1.0
 */
@DisplayName("AccountService 單元測試")
class AccountServiceTest {
    
    private AccountService accountService;
    
    @BeforeEach
    void setUp() {
        accountService = new AccountService();
    }
    
    // ==================== 測試 getBalance() ====================
    
    @Test
    @DisplayName("測試查詢存在的帳戶餘額 - ACC001")
    void testGetBalance_ExistingAccount_ACC001() {
        // Given
        String accountId = "ACC001";
        BigDecimal expectedBalance = new BigDecimal("50000.00");
        
        // When
        BigDecimal actualBalance = accountService.getBalance(accountId);
        
        // Then
        assertNotNull(actualBalance, "餘額不應為 null");
        assertEquals(expectedBalance, actualBalance, "ACC001 的餘額應為 50000.00");
    }
    
    @Test
    @DisplayName("測試查詢存在的帳戶餘額 - ACC002")
    void testGetBalance_ExistingAccount_ACC002() {
        // Given
        String accountId = "ACC002";
        BigDecimal expectedBalance = new BigDecimal("120000.00");
        
        // When
        BigDecimal actualBalance = accountService.getBalance(accountId);
        
        // Then
        assertNotNull(actualBalance, "餘額不應為 null");
        assertEquals(expectedBalance, actualBalance, "ACC002 的餘額應為 120000.00");
    }
    
    @Test
    @DisplayName("測試查詢存在的帳戶餘額 - ACC003")
    void testGetBalance_ExistingAccount_ACC003() {
        // Given
        String accountId = "ACC003";
        BigDecimal expectedBalance = new BigDecimal("8500.50");
        
        // When
        BigDecimal actualBalance = accountService.getBalance(accountId);
        
        // Then
        assertNotNull(actualBalance, "餘額不應為 null");
        assertEquals(expectedBalance, actualBalance, "ACC003 的餘額應為 8500.50");
    }
    
    @Test
    @DisplayName("測試查詢不存在的帳戶餘額 - 應回傳 0")
    void testGetBalance_NonExistingAccount_ReturnsZero() {
        // Given
        String accountId = "ACC999";
        BigDecimal expectedBalance = BigDecimal.ZERO;
        
        // When
        BigDecimal actualBalance = accountService.getBalance(accountId);
        
        // Then
        assertNotNull(actualBalance, "餘額不應為 null");
        assertEquals(expectedBalance, actualBalance, "不存在的帳戶應回傳 0");
    }
    
    @Test
    @DisplayName("測試查詢空字串帳號 - 應回傳 0")
    void testGetBalance_EmptyAccountId_ReturnsZero() {
        // Given
        String accountId = "";
        BigDecimal expectedBalance = BigDecimal.ZERO;
        
        // When
        BigDecimal actualBalance = accountService.getBalance(accountId);
        
        // Then
        assertEquals(expectedBalance, actualBalance, "空字串帳號應回傳 0");
    }
    
    // ==================== 測試 accountExists() ====================
    
    @Test
    @DisplayName("測試帳戶存在檢查 - ACC001 存在")
    void testAccountExists_ACC001_ReturnsTrue() {
        // Given
        String accountId = "ACC001";
        
        // When
        boolean exists = accountService.accountExists(accountId);
        
        // Then
        assertTrue(exists, "ACC001 應該存在");
    }
    
    @Test
    @DisplayName("測試帳戶存在檢查 - ACC002 存在")
    void testAccountExists_ACC002_ReturnsTrue() {
        // Given
        String accountId = "ACC002";
        
        // When
        boolean exists = accountService.accountExists(accountId);
        
        // Then
        assertTrue(exists, "ACC002 應該存在");
    }
    
    @Test
    @DisplayName("測試帳戶存在檢查 - ACC003 存在")
    void testAccountExists_ACC003_ReturnsTrue() {
        // Given
        String accountId = "ACC003";
        
        // When
        boolean exists = accountService.accountExists(accountId);
        
        // Then
        assertTrue(exists, "ACC003 應該存在");
    }
    
    @Test
    @DisplayName("測試帳戶存在檢查 - ACC999 不存在")
    void testAccountExists_NonExistingAccount_ReturnsFalse() {
        // Given
        String accountId = "ACC999";
        
        // When
        boolean exists = accountService.accountExists(accountId);
        
        // Then
        assertFalse(exists, "ACC999 不應該存在");
    }
    
    @Test
    @DisplayName("測試帳戶存在檢查 - 空字串不存在")
    void testAccountExists_EmptyAccountId_ReturnsFalse() {
        // Given
        String accountId = "";
        
        // When
        boolean exists = accountService.accountExists(accountId);
        
        // Then
        assertFalse(exists, "空字串帳號不應該存在");
    }
    
    // ==================== 測試 getRecentTransactions() ====================
    
    @Test
    @DisplayName("測試查詢交易記錄 - 應回傳 3 筆交易")
    void testGetRecentTransactions_ReturnsThreeTransactions() {
        // Given
        String accountId = "ACC001";
        
        // When
        List<Transaction> transactions = accountService.getRecentTransactions(accountId);
        
        // Then
        assertNotNull(transactions, "交易記錄不應為 null");
        assertEquals(3, transactions.size(), "應回傳 3 筆交易記錄");
    }
    
    @Test
    @DisplayName("測試交易記錄內容 - 第一筆交易（ATM 提款）")
    void testGetRecentTransactions_FirstTransaction_ATMWithdrawal() {
        // Given
        String accountId = "ACC001";
        
        // When
        List<Transaction> transactions = accountService.getRecentTransactions(accountId);
        Transaction firstTransaction = transactions.get(0);
        
        // Then
        assertNotNull(firstTransaction, "第一筆交易不應為 null");
        assertEquals(new BigDecimal("-500"), firstTransaction.getAmount(), "第一筆交易金額應為 -500");
        assertEquals("ATM 提款", firstTransaction.getDescription(), "第一筆交易描述應為 'ATM 提款'");
        assertNotNull(firstTransaction.getDate(), "交易日期不應為 null");
        assertNotNull(firstTransaction.getBalance(), "交易後餘額不應為 null");
    }
    
    @Test
    @DisplayName("測試交易記錄內容 - 第二筆交易（薪資入帳）")
    void testGetRecentTransactions_SecondTransaction_SalaryDeposit() {
        // Given
        String accountId = "ACC001";
        
        // When
        List<Transaction> transactions = accountService.getRecentTransactions(accountId);
        Transaction secondTransaction = transactions.get(1);
        
        // Then
        assertNotNull(secondTransaction, "第二筆交易不應為 null");
        assertEquals(new BigDecimal("3000"), secondTransaction.getAmount(), "第二筆交易金額應為 3000");
        assertEquals("薪資入帳", secondTransaction.getDescription(), "第二筆交易描述應為 '薪資入帳'");
        assertNotNull(secondTransaction.getDate(), "交易日期不應為 null");
        assertNotNull(secondTransaction.getBalance(), "交易後餘額不應為 null");
    }
    
    @Test
    @DisplayName("測試交易記錄內容 - 第三筆交易（信用卡繳費）")
    void testGetRecentTransactions_ThirdTransaction_CreditCardPayment() {
        // Given
        String accountId = "ACC001";
        
        // When
        List<Transaction> transactions = accountService.getRecentTransactions(accountId);
        Transaction thirdTransaction = transactions.get(2);
        
        // Then
        assertNotNull(thirdTransaction, "第三筆交易不應為 null");
        assertEquals(new BigDecimal("-1200"), thirdTransaction.getAmount(), "第三筆交易金額應為 -1200");
        assertEquals("信用卡繳費", thirdTransaction.getDescription(), "第三筆交易描述應為 '信用卡繳費'");
        assertNotNull(thirdTransaction.getDate(), "交易日期不應為 null");
        assertNotNull(thirdTransaction.getBalance(), "交易後餘額不應為 null");
    }
    
    @Test
    @DisplayName("測試交易記錄時間順序 - 應按時間倒序排列")
    void testGetRecentTransactions_OrderedByDateDescending() {
        // Given
        String accountId = "ACC001";
        
        // When
        List<Transaction> transactions = accountService.getRecentTransactions(accountId);
        
        // Then
        assertTrue(
            transactions.get(0).getDate().isAfter(transactions.get(1).getDate()),
            "第一筆交易應比第二筆交易新"
        );
        assertTrue(
            transactions.get(1).getDate().isAfter(transactions.get(2).getDate()),
            "第二筆交易應比第三筆交易新"
        );
    }
    
    @Test
    @DisplayName("測試不同帳戶的交易記錄 - ACC002")
    void testGetRecentTransactions_DifferentAccount_ACC002() {
        // Given
        String accountId = "ACC002";
        BigDecimal expectedBalance = new BigDecimal("120000.00");
        
        // When
        List<Transaction> transactions = accountService.getRecentTransactions(accountId);
        Transaction firstTransaction = transactions.get(0);
        
        // Then
        assertEquals(3, transactions.size(), "應回傳 3 筆交易記錄");
        assertEquals(expectedBalance, firstTransaction.getBalance(), "第一筆交易後餘額應為 120000.00");
    }
    
    @Test
    @DisplayName("測試交易記錄的所有欄位都不為 null")
    void testGetRecentTransactions_AllFieldsNotNull() {
        // Given
        String accountId = "ACC001";
        
        // When
        List<Transaction> transactions = accountService.getRecentTransactions(accountId);
        
        // Then
        for (Transaction transaction : transactions) {
            assertNotNull(transaction.getDate(), "交易日期不應為 null");
            assertNotNull(transaction.getAmount(), "交易金額不應為 null");
            assertNotNull(transaction.getDescription(), "交易描述不應為 null");
            assertNotNull(transaction.getBalance(), "交易後餘額不應為 null");
            assertFalse(transaction.getDescription().isEmpty(), "交易描述不應為空字串");
        }
    }
}

// Made with Bob
