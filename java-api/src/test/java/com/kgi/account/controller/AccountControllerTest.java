package com.kgi.account.controller;

import com.kgi.account.model.BalanceResponse;
import com.kgi.account.model.Transaction;
import com.kgi.account.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * AccountController 單元測試
 * 
 * 使用 Mockito 模擬 AccountService，測試 REST API 控制器的行為
 * 
 * @author Bob AI
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AccountController 單元測試")
class AccountControllerTest {
    
    @Mock
    private AccountService accountService;
    
    @InjectMocks
    private AccountController accountController;
    
    // ==================== 測試 getBalance() - 成功案例 ====================
    
    @Test
    @DisplayName("測試查詢餘額 - ACC001 成功")
    void testGetBalance_ExistingAccount_ACC001_ReturnsOk() {
        // Given
        String accountId = "ACC001";
        BigDecimal balance = new BigDecimal("50000.00");
        
        when(accountService.getBalance(accountId)).thenReturn(balance);
        when(accountService.accountExists(accountId)).thenReturn(true);
        
        // When
        ResponseEntity<?> response = accountController.getBalance(accountId);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "狀態碼應為 200 OK");
        assertNotNull(response.getBody(), "回應內容不應為 null");
        assertTrue(response.getBody() instanceof BalanceResponse, "回應應為 BalanceResponse 型別");
        
        BalanceResponse balanceResponse = (BalanceResponse) response.getBody();
        assertEquals(accountId, balanceResponse.getAccountId(), "帳號應為 ACC001");
        assertEquals(balance, balanceResponse.getBalance(), "餘額應為 50000.00");
        assertEquals("TWD", balanceResponse.getCurrency(), "幣別應為 TWD");
        
        // Verify
        verify(accountService, times(1)).getBalance(accountId);
        verify(accountService, times(1)).accountExists(accountId);
    }
    
    @Test
    @DisplayName("測試查詢餘額 - ACC002 成功")
    void testGetBalance_ExistingAccount_ACC002_ReturnsOk() {
        // Given
        String accountId = "ACC002";
        BigDecimal balance = new BigDecimal("120000.00");
        
        when(accountService.getBalance(accountId)).thenReturn(balance);
        when(accountService.accountExists(accountId)).thenReturn(true);
        
        // When
        ResponseEntity<?> response = accountController.getBalance(accountId);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "狀態碼應為 200 OK");
        BalanceResponse balanceResponse = (BalanceResponse) response.getBody();
        assertNotNull(balanceResponse);
        assertEquals(balance, balanceResponse.getBalance(), "餘額應為 120000.00");
        
        verify(accountService).getBalance(accountId);
        verify(accountService).accountExists(accountId);
    }
    
    @Test
    @DisplayName("測試查詢餘額 - ACC003 成功")
    void testGetBalance_ExistingAccount_ACC003_ReturnsOk() {
        // Given
        String accountId = "ACC003";
        BigDecimal balance = new BigDecimal("8500.50");
        
        when(accountService.getBalance(accountId)).thenReturn(balance);
        when(accountService.accountExists(accountId)).thenReturn(true);
        
        // When
        ResponseEntity<?> response = accountController.getBalance(accountId);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "狀態碼應為 200 OK");
        BalanceResponse balanceResponse = (BalanceResponse) response.getBody();
        assertNotNull(balanceResponse);
        assertEquals(balance, balanceResponse.getBalance(), "餘額應為 8500.50");
        
        verify(accountService).getBalance(accountId);
        verify(accountService).accountExists(accountId);
    }
    
    // ==================== 測試 getBalance() - 失敗案例 ====================
    
    @Test
    @DisplayName("測試查詢餘額 - 帳戶不存在應回傳 404")
    void testGetBalance_NonExistingAccount_ReturnsNotFound() {
        // Given
        String accountId = "ACC999";
        
        when(accountService.getBalance(accountId)).thenReturn(BigDecimal.ZERO);
        when(accountService.accountExists(accountId)).thenReturn(false);
        
        // When
        ResponseEntity<?> response = accountController.getBalance(accountId);
        
        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "狀態碼應為 404 NOT_FOUND");
        assertNotNull(response.getBody(), "回應內容不應為 null");
        assertTrue(response.getBody() instanceof Map, "回應應為 Map 型別");
        
        @SuppressWarnings("unchecked")
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("帳戶不存在", errorResponse.get("error"), "錯誤訊息應為 '帳戶不存在'");
        assertEquals(accountId, errorResponse.get("accountId"), "應包含帳號資訊");
        
        verify(accountService).getBalance(accountId);
        verify(accountService).accountExists(accountId);
    }
    
    @Test
    @DisplayName("測試查詢餘額 - 餘額為 0 但帳戶存在應回傳 200")
    void testGetBalance_ZeroBalanceButAccountExists_ReturnsOk() {
        // Given
        String accountId = "ACC004";
        BigDecimal balance = BigDecimal.ZERO;
        
        when(accountService.getBalance(accountId)).thenReturn(balance);
        when(accountService.accountExists(accountId)).thenReturn(true);
        
        // When
        ResponseEntity<?> response = accountController.getBalance(accountId);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "狀態碼應為 200 OK");
        BalanceResponse balanceResponse = (BalanceResponse) response.getBody();
        assertNotNull(balanceResponse);
        assertEquals(BigDecimal.ZERO, balanceResponse.getBalance(), "餘額應為 0");
        
        verify(accountService).getBalance(accountId);
        verify(accountService).accountExists(accountId);
    }
    
    // ==================== 測試 getTransactions() - 成功案例 ====================
    
    @Test
    @DisplayName("測試查詢交易記錄 - 成功回傳 3 筆交易")
    void testGetTransactions_ExistingAccount_ReturnsThreeTransactions() {
        // Given
        String accountId = "ACC001";
        List<Transaction> mockTransactions = Arrays.asList(
            new Transaction(
                LocalDateTime.now().minusDays(1),
                new BigDecimal("-500"),
                "ATM 提款",
                new BigDecimal("50000.00")
            ),
            new Transaction(
                LocalDateTime.now().minusDays(2),
                new BigDecimal("3000"),
                "薪資入帳",
                new BigDecimal("50500.00")
            ),
            new Transaction(
                LocalDateTime.now().minusDays(5),
                new BigDecimal("-1200"),
                "信用卡繳費",
                new BigDecimal("47500.00")
            )
        );
        
        when(accountService.accountExists(accountId)).thenReturn(true);
        when(accountService.getRecentTransactions(accountId)).thenReturn(mockTransactions);
        
        // When
        ResponseEntity<?> response = accountController.getTransactions(accountId);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "狀態碼應為 200 OK");
        assertNotNull(response.getBody(), "回應內容不應為 null");
        assertTrue(response.getBody() instanceof List, "回應應為 List 型別");
        
        @SuppressWarnings("unchecked")
        List<Transaction> transactions = (List<Transaction>) response.getBody();
        assertEquals(3, transactions.size(), "應回傳 3 筆交易記錄");
        
        // 驗證第一筆交易
        Transaction firstTransaction = transactions.get(0);
        assertEquals(new BigDecimal("-500"), firstTransaction.getAmount(), "第一筆交易金額應為 -500");
        assertEquals("ATM 提款", firstTransaction.getDescription(), "第一筆交易描述應為 'ATM 提款'");
        
        verify(accountService).accountExists(accountId);
        verify(accountService).getRecentTransactions(accountId);
    }
    
    @Test
    @DisplayName("測試查詢交易記錄 - 驗證所有交易欄位")
    void testGetTransactions_VerifyAllTransactionFields() {
        // Given
        String accountId = "ACC001";
        LocalDateTime transactionDate = LocalDateTime.now().minusDays(1);
        List<Transaction> mockTransactions = Arrays.asList(
            new Transaction(
                transactionDate,
                new BigDecimal("-500"),
                "ATM 提款",
                new BigDecimal("50000.00")
            )
        );
        
        when(accountService.accountExists(accountId)).thenReturn(true);
        when(accountService.getRecentTransactions(accountId)).thenReturn(mockTransactions);
        
        // When
        ResponseEntity<?> response = accountController.getTransactions(accountId);
        
        // Then
        @SuppressWarnings("unchecked")
        List<Transaction> transactions = (List<Transaction>) response.getBody();
        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        
        Transaction transaction = transactions.get(0);
        assertNotNull(transaction.getDate(), "交易日期不應為 null");
        assertNotNull(transaction.getAmount(), "交易金額不應為 null");
        assertNotNull(transaction.getDescription(), "交易描述不應為 null");
        assertNotNull(transaction.getBalance(), "交易後餘額不應為 null");
        
        verify(accountService).accountExists(accountId);
        verify(accountService).getRecentTransactions(accountId);
    }
    
    // ==================== 測試 getTransactions() - 失敗案例 ====================
    
    @Test
    @DisplayName("測試查詢交易記錄 - 帳戶不存在應回傳 404")
    void testGetTransactions_NonExistingAccount_ReturnsNotFound() {
        // Given
        String accountId = "ACC999";
        
        when(accountService.accountExists(accountId)).thenReturn(false);
        
        // When
        ResponseEntity<?> response = accountController.getTransactions(accountId);
        
        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "狀態碼應為 404 NOT_FOUND");
        assertNotNull(response.getBody(), "回應內容不應為 null");
        assertTrue(response.getBody() instanceof Map, "回應應為 Map 型別");
        
        @SuppressWarnings("unchecked")
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("帳戶不存在", errorResponse.get("error"), "錯誤訊息應為 '帳戶不存在'");
        assertEquals(accountId, errorResponse.get("accountId"), "應包含帳號資訊");
        
        verify(accountService).accountExists(accountId);
        verify(accountService, never()).getRecentTransactions(accountId);
    }
    
    @Test
    @DisplayName("測試查詢交易記錄 - 空字串帳號應回傳 404")
    void testGetTransactions_EmptyAccountId_ReturnsNotFound() {
        // Given
        String accountId = "";
        
        when(accountService.accountExists(accountId)).thenReturn(false);
        
        // When
        ResponseEntity<?> response = accountController.getTransactions(accountId);
        
        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "狀態碼應為 404 NOT_FOUND");
        
        verify(accountService).accountExists(accountId);
        verify(accountService, never()).getRecentTransactions(accountId);
    }
    
    // ==================== 測試 Service 互動 ====================
    
    @Test
    @DisplayName("測試 getBalance 只呼叫一次 accountExists")
    void testGetBalance_CallsAccountExistsOnce() {
        // Given
        String accountId = "ACC001";
        when(accountService.getBalance(accountId)).thenReturn(new BigDecimal("50000.00"));
        when(accountService.accountExists(accountId)).thenReturn(true);
        
        // When
        accountController.getBalance(accountId);
        
        // Then
        verify(accountService, times(1)).accountExists(accountId);
    }
    
    @Test
    @DisplayName("測試 getBalance 只呼叫一次 getBalance")
    void testGetBalance_CallsGetBalanceOnce() {
        // Given
        String accountId = "ACC001";
        when(accountService.getBalance(accountId)).thenReturn(new BigDecimal("50000.00"));
        when(accountService.accountExists(accountId)).thenReturn(true);
        
        // When
        accountController.getBalance(accountId);
        
        // Then
        verify(accountService, times(1)).getBalance(accountId);
    }
    
    @Test
    @DisplayName("測試 getTransactions 帳戶不存在時不呼叫 getRecentTransactions")
    void testGetTransactions_AccountNotExists_DoesNotCallGetRecentTransactions() {
        // Given
        String accountId = "ACC999";
        when(accountService.accountExists(accountId)).thenReturn(false);
        
        // When
        accountController.getTransactions(accountId);
        
        // Then
        verify(accountService, never()).getRecentTransactions(accountId);
    }
    
    @Test
    @DisplayName("測試 getTransactions 帳戶存在時呼叫 getRecentTransactions")
    void testGetTransactions_AccountExists_CallsGetRecentTransactions() {
        // Given
        String accountId = "ACC001";
        when(accountService.accountExists(accountId)).thenReturn(true);
        when(accountService.getRecentTransactions(accountId)).thenReturn(Arrays.asList());
        
        // When
        accountController.getTransactions(accountId);
        
        // Then
        verify(accountService, times(1)).getRecentTransactions(accountId);
    }
}

// Made with Bob
