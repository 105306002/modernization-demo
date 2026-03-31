# 帳戶查詢 API (Java/Spring Boot 版本)

## 📋 專案說明

這是從 .NET Core 遷移到 Java/Spring Boot 的帳戶查詢 API，提供帳戶餘額查詢和交易記錄查詢功能。

**遷移來源：** .NET Core 6.0  
**目標平台：** Java 17 + Spring Boot 3.2.x  
**建構工具：** Maven 3.9.x

---

## 🏗️ 專案結構

```
java-api/
├── src/
│   ├── main/
│   │   ├── java/com/kgi/account/
│   │   │   ├── AccountApplication.java          # 應用程式進入點
│   │   │   ├── controller/
│   │   │   │   └── AccountController.java       # REST 控制器
│   │   │   ├── service/
│   │   │   │   └── AccountService.java          # 業務邏輯服務
│   │   │   ├── model/
│   │   │   │   ├── BalanceResponse.java         # 餘額回應 DTO
│   │   │   │   ├── Transaction.java             # 交易資料模型
│   │   │   │   └── ErrorResponse.java           # 錯誤回應 DTO
│   │   │   ├── config/
│   │   │   │   └── CorsConfig.java              # CORS 配置
│   │   │   └── exception/
│   │   │       ├── AccountNotFoundException.java    # 自訂例外
│   │   │       └── GlobalExceptionHandler.java      # 全域例外處理器
│   │   └── resources/
│   │       └── application.yml                  # 應用程式配置
│   └── test/
│       └── java/com/kgi/account/
│           └── (測試檔案)
├── pom.xml                                      # Maven 配置
└── README.md                                    # 本檔案
```

---

## 🚀 快速開始

### 環境需求

- **Java:** 17 或以上
- **Maven:** 3.9.x 或以上
- **IDE:** IntelliJ IDEA / Eclipse / VS Code (推薦)

### 安裝步驟

1. **Clone 專案**
```bash
git clone https://github.com/105306002/modernization-demo.git
cd modernization-demo/java-api
```

2. **編譯專案**
```bash
mvn clean install
```

3. **啟動應用程式**
```bash
mvn spring-boot:run
```

4. **驗證啟動**
應用程式啟動後會顯示：
```
凱基銀行帳戶查詢 API (Java/Spring Boot) 已啟動
API 端點：http://localhost:8080
```

---

## 📡 API 端點

### 1. 查詢帳戶餘額

**端點：** `GET /api/account/{accountId}/balance`

**範例請求：**
```bash
curl http://localhost:8080/api/account/ACC001/balance
```

**成功回應 (200 OK)：**
```json
{
  "accountId": "ACC001",
  "balance": 50000.00,
  "currency": "TWD"
}
```

**失敗回應 (404 Not Found)：**
```json
{
  "error": "帳戶不存在",
  "accountId": "ACC999"
}
```

### 2. 查詢交易記錄

**端點：** `GET /api/account/{accountId}/transactions`

**範例請求：**
```bash
curl http://localhost:8080/api/account/ACC001/transactions
```

**成功回應 (200 OK)：**
```json
[
  {
    "date": "2024-03-30T10:30:00",
    "amount": -500,
    "description": "ATM 提款",
    "balance": 50000.00
  },
  {
    "date": "2024-03-29T14:20:00",
    "amount": 3000,
    "description": "薪資入帳",
    "balance": 50500.00
  },
  {
    "date": "2024-03-26T09:15:00",
    "amount": -1200,
    "description": "信用卡繳費",
    "balance": 47500.00
  }
]
```

**失敗回應 (404 Not Found)：**
```json
{
  "error": "帳戶不存在",
  "accountId": "ACC999"
}
```

---

## 🧪 測試資料

專案使用記憶體內的 Map 模擬帳戶資料：

| 帳號 | 戶名 | 餘額 (TWD) |
|------|------|-----------|
| ACC001 | 張先生 | 50,000.00 |
| ACC002 | 李小姐 | 120,000.00 |
| ACC003 | 王先生 | 8,500.50 |

---

## 🔧 技術架構

### 核心技術

- **Spring Boot 3.2.3** - 應用程式框架
- **Spring Web** - RESTful API 支援
- **Lombok** - 減少樣板程式碼
- **Maven** - 依賴管理和建構工具

### 架構模式

- **MVC 模式** - Model-View-Controller
- **分層架構** - Controller → Service → Data
- **依賴注入** - Spring IoC Container

### 關鍵設計

1. **BigDecimal 用於金額** - 確保精度，避免浮點數誤差
2. **LocalDateTime 用於日期** - Java 8+ 時間 API
3. **全域例外處理** - 統一的錯誤回應格式
4. **CORS 配置** - 支援跨域請求

---

## 🔄 與 .NET Core 版本的對照

| 層面 | .NET Core | Java/Spring Boot |
|------|-----------|------------------|
| **框架** | ASP.NET Core 6.0 | Spring Boot 3.2.x |
| **語言** | C# 10 | Java 17 |
| **控制器** | `[ApiController]` | `@RestController` |
| **路由** | `[Route]` | `@RequestMapping` |
| **GET 端點** | `[HttpGet]` | `@GetMapping` |
| **金額型別** | `decimal` | `BigDecimal` |
| **日期時間** | `DateTime` | `LocalDateTime` |
| **依賴注入** | 建構子注入 | `@Autowired` |
| **回應** | `IActionResult` | `ResponseEntity<?>` |

---

## 📝 開發指南

### 新增 API 端點

1. 在 `AccountController.java` 新增方法
2. 使用 `@GetMapping` 或 `@PostMapping` 註解
3. 注入 `AccountService` 處理業務邏輯
4. 回傳 `ResponseEntity<?>` 物件

### 新增業務邏輯

1. 在 `AccountService.java` 新增方法
2. 使用 `@Service` 註解標記類別
3. 實作業務邏輯
4. 回傳適當的資料型別

### 新增資料模型

1. 在 `model` 套件建立新類別
2. 使用 Lombok 註解 (`@Data`, `@AllArgsConstructor`, `@NoArgsConstructor`)
3. 定義欄位和型別

---

## 🧪 測試

### 執行測試

```bash
mvn test
```

### 測試覆蓋率

```bash
mvn clean test jacoco:report
```

報告位置：`target/site/jacoco/index.html`

---

## 📦 打包部署

### 建立 JAR 檔案

```bash
mvn clean package
```

JAR 檔案位置：`target/account-api-1.0.0.jar`

### 執行 JAR 檔案

```bash
java -jar target/account-api-1.0.0.jar
```

---

## 🔍 疑難排解

### 問題：Port 8080 已被佔用

**解決方法：** 修改 `application.yml` 中的 `server.port`

```yaml
server:
  port: 8081  # 改為其他 port
```

### 問題：Lombok 註解無法識別

**解決方法：** 
1. 確認 IDE 已安裝 Lombok 外掛
2. 啟用 Annotation Processing

### 問題：Maven 依賴下載失敗

**解決方法：**
```bash
mvn clean install -U
```

---

## 📚 參考資源

- [Spring Boot 官方文檔](https://spring.io/projects/spring-boot)
- [Spring Web MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
- [Java BigDecimal 最佳實踐](https://docs.oracle.com/javase/8/docs/api/java/math/BigDecimal.html)
- [Lombok 使用指南](https://projectlombok.org/)

---

## ✅ 驗收標準

### 功能驗收

- [x] GET `/api/account/{accountId}/balance` 端點正常運作
- [x] GET `/api/account/{accountId}/transactions` 端點正常運作
- [x] 回應格式與 .NET Core 版本一致
- [x] 測試資料正確（ACC001, ACC002, ACC003）
- [x] 錯誤處理正確（404 Not Found）

### 技術驗收

- [x] 使用 BigDecimal 處理金額
- [x] 使用 LocalDateTime 處理日期時間
- [x] 實作 CORS 配置
- [x] 實作全域例外處理
- [x] 程式碼註解完整

---

## 👥 貢獻者

- **開發團隊** - IBM Bob Workshop
- **AI 輔助** - Bob AI

---

## 📄 授權

本專案僅供教學和示範使用。

---

**版本：** 1.0.0  
**最後更新：** 2026-03-31  
**狀態：** ✅ 已完成
