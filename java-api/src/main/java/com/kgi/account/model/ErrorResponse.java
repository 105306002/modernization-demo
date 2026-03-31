package com.kgi.account.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 錯誤回應 DTO
 * 
 * 用於統一的錯誤訊息格式
 * 
 * @author Bob AI
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    
    /**
     * 錯誤訊息
     */
    private String error;
    
    /**
     * 錯誤詳細資訊
     */
    private String details;
    
    /**
     * 錯誤發生時間
     */
    private LocalDateTime timestamp;
}

// Made with Bob
