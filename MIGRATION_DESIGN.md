# .NET Core 到 Java/Spring Boot 遷移設計文件

## 📋 專案資訊

- **Issue:** [#3 將.net轉換為JAVA](https://github.com/105306002/modernization-demo/issues/3)
- **專案名稱:** modernization-demo
- **遷移目標:** 將 .NET Core 6.0 帳戶查詢 API 遷移到 Java/Spring Boot
- **文件版本:** 1.0
- **建立日期:** 2026-03-31

---

## 🎯 遷移目標

將現有的 .NET Core 帳戶查詢 API 完整遷移到 Java/Spring Boot 平台，確保：
1. API 功能完全相容
2. 業務邏輯保持一致
3. 回應格式完全相同
4. 效能不低於原系統

---

## 📊 現況分析

### .NET Core 原始系統架構

#### 技術棧
- **框架:** ASP.NET Core 6.0
- **語言:** C# 10
- **架構模式:** MVC (Controller-Service)
- **依賴注入:** 內建 DI Container
- **資料儲存:** 記憶體內 Dictionary（模擬）

#### 核心元件
1. **Program.cs** - 應用程式進入點與配置
2. **AccountController.cs** - API 端點定義（2 個端點）
3. **AccountService.cs** - 業務邏輯實作
4. **Transaction.cs** - 交易資料模型

#### API 端點
| 端點 | 方法 | 路徑 | 功能 |
|------|------|------|------|
| 查詢餘額 | GET | `/api/account/{accountId}/balance` | 查詢帳戶當前餘額 |
| 查詢交易 | GET | `/api/account/{accountId}/transactions` | 查詢近期交易記錄 |

#### 測試資料
```csharp
ACC001 -> 50,000.00 TWD (張先生)
ACC002 -> 120,000.00 TWD (李小姐)
ACC003 -> 8,500.50 TWD (王先生)
```

---

## 🏗️ Java/Spring Boot 目標架構設計

### 技術棧選擇

#### 核心框架
- **框架:** Spring Boot 3.2.x
- **語言:** Java 17 (LTS)
- **建構工具:** Maven 3.9.x
- **架構模式:** MVC (Controller-Service-Repository)

#### 關鍵依賴
```xml
<dependencies>
    <!-- Spring Boot Web Starter -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- Lombok (減少樣板程式碼) -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    
    <!-- Spring Boot Test -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### 專案結構設計

```
java-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── kgi/
│   │   │           └── account/
│   │   │               ├── AccountApplication.java          # 應用程式進入點
│   │   │               ├── controller/
│   │   │               │   └── AccountController.java       # REST 控制器
│   │   │               ├── service/
│   │   │               │   └── AccountService.java          # 業務邏輯服務
│   │   │               ├── model/
│   │   │               │   ├── BalanceResponse.java         # 餘額回應 DTO
│   │   │               │   └── Transaction.java             # 交易資料模型
│   │   │               └── config/
│   │   │                   └── CorsConfig.java              # CORS 配置
│   │   └── resources/
│   │       └── application.yml                              # 應用程式配置
│   └── test/
│       └── java/
│           └── com/
│               └── kgi/
│                   └── account/
│                       ├── controller/
│                       │   └── AccountControllerTest.java   # 控制器測試
│                       └── service/
│                           └── AccountServiceTest.java      # 服務測試
├── pom.xml                                                  # Maven 配置
└── README.md                                                # 專案說明
```

---

## 🔄 詳細轉換對照

### 1. 應用程式進入點

#### .NET Core (Program.cs)
```csharp
var builder = WebApplication.CreateBuilder(args);
builder.Services.AddControllers();
builder.Services.AddSingleton<AccountService>();

var app = builder.Build();
app.UseCors(policy => policy.AllowAnyOrigin().AllowAnyMethod().AllowAnyHeader());
app.MapControllers();
app.Run();
```

#### Java/Spring Boot (AccountApplication.java)
```java
@SpringBootApplication
public class AccountApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class, args);
        System.out.println("凱基銀行帳戶查詢 API (Java/Spring Boot) 已啟動");
        System.out.println("API 端點：http://localhost:8080");
    }
}
```

#### CORS 配置 (CorsConfig.java)
```java
@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*")
                        .allowedHeaders("*");
            }
        };
    }
}
```

### 2. 控制器層

#### .NET Core (AccountController.cs)
```csharp
[ApiController]
[Route("api/account")]
public class AccountController : ControllerBase
{
    private readonly AccountService _service;
    
    public AccountController(AccountService service)
    {
        _service = service;
    }
    
    [HttpGet("{accountId}/balance")]
    public IActionResult GetBalance(string accountId)
    {
        var balance = _service.GetBalance(accountId);
        if (balance == 0 && !_service.AccountExists(accountId))
        {
            return NotFound(new { error = "帳戶不存在", accountId });
        }
        return Ok(new { accountId, balance, currency = "TWD" });
    }
}
```

#### Java/Spring Boot (AccountController.java)
```java
@RestController
@RequestMapping("/api/account")
public class AccountController {
    
    private final AccountService service;
    
    @Autowired
    public AccountController(AccountService service) {
        this.service = service;
    }
    
    @GetMapping("/{accountId}/balance")
    public ResponseEntity<?> getBalance(@PathVariable String accountId) {
        BigDecimal balance = service.getBalance(accountId);
        if (balance.compareTo(BigDecimal.ZERO) == 0 && !service.accountExists(accountId)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "帳戶不存在");
            error.put("accountId", accountId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        
        BalanceResponse response = new BalanceResponse(accountId, balance, "TWD");
        return ResponseEntity.ok(response);
    }
}
```

### 3. 服務層

#### .NET Core (AccountService.cs)
```csharp
public class AccountService
{
    private static readonly Dictionary<string, decimal> _accounts = new()
    {
        { "ACC001", 50000.00m },
        { "ACC002", 120000.00m },
        { "ACC003", 8500.50m }
    };
    
    public decimal GetBalance(string accountId)
    {
        return _accounts.GetValueOrDefault(accountId, 0);
    }
    
    public bool AccountExists(string accountId)
    {
        return _accounts.ContainsKey(accountId);
    }
}
```

#### Java/Spring Boot (AccountService.java)
```java
@Service
public class AccountService {
    
    private static final Map<String, BigDecimal> accounts = new HashMap<>();
    
    static {
        accounts.put("ACC001", new BigDecimal("50000.00"));
        accounts.put("ACC002", new BigDecimal("120000.00"));
        accounts.put("ACC003", new BigDecimal("8500.50"));
    }
    
    public BigDecimal getBalance(String accountId) {
        return accounts.getOrDefault(accountId, BigDecimal.ZERO);
    }
    
    public boolean accountExists(String accountId) {
        return accounts.containsKey(accountId);
    }
    
    public List<Transaction> getRecentTransactions(String accountId) {
        BigDecimal currentBalance = getBalance(accountId);
        List<Transaction> transactions = new ArrayList<>();
        
        transactions.add(new Transaction(
            LocalDateTime.now().minusDays(1),
            new BigDecimal("-500"),
            "ATM 提款",
            currentBalance
        ));
        
        transactions.add(new Transaction(
            LocalDateTime.now().minusDays(2),
            new BigDecimal("3000"),
            "薪資入帳",
            currentBalance.add(new BigDecimal("500"))
        ));
        
        transactions.add(new Transaction(
            LocalDateTime.now().minusDays(5),
            new BigDecimal("-1200"),
            "信用卡繳費",
            currentBalance.add(new BigDecimal("500")).subtract(new BigDecimal("3000"))
        ));
        
        return transactions;
    }
}
```

### 4. 資料模型

#### .NET Core (Transaction.cs)
```csharp
public class Transaction
{
    public DateTime Date { get; set; }
    public decimal Amount { get; set; }
    public string Description { get; set; } = string.Empty;
    public decimal Balance { get; set; }
}
```

#### Java/Spring Boot (Transaction.java)
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private LocalDateTime date;
    private BigDecimal amount;
    private String description;
    private BigDecimal balance;
}
```

#### Java/Spring Boot (BalanceResponse.java)
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceResponse {
    private String accountId;
    private BigDecimal balance;
    private String currency;
}
```

### 5. 配置檔案

#### .NET Core (Account.API.csproj)
```xml
<Project Sdk="Microsoft.NET.Sdk.Web">
  <PropertyGroup>
    <TargetFramework>net6.0</TargetFramework>
  </PropertyGroup>
</Project>
```

#### Java/Spring Boot (application.yml)
```yaml
server:
  port: 8080

spring:
  application:
    name: account-api
  jackson:
    serialization:
      write-dates-as-timestamps: false
    default-property-inclusion: non_null

logging:
  level:
    com.kgi.account: INFO
```

---

## ✅ 驗收標準 (Acceptance Criteria)

### 功能性驗收標準

#### AC1: API 端點相容性
- [ ] **AC1.1** - GET `/api/account/{accountId}/balance` 端點正常運作
- [ ] **AC1.2** - GET `/api/account/{accountId}/transactions` 端點正常運作
- [ ] **AC1.3** - 路徑參數 `{accountId}` 正確解析
- [ ] **AC1.4** - HTTP 方法為 GET

#### AC2: 查詢餘額功能
- [ ] **AC2.1** - 輸入 `ACC001` 回傳餘額 `50000.00`
- [ ] **AC2.2** - 輸入 `ACC002` 回傳餘額 `120000.00`
- [ ] **AC2.3** - 輸入 `ACC003` 回傳餘額 `8500.50`
- [ ] **AC2.4** - 回應包含 `accountId`, `balance`, `currency` 欄位
- [ ] **AC2.5** - `currency` 固定為 `"TWD"`
- [ ] **AC2.6** - 不存在的帳號回傳 404 狀態碼
- [ ] **AC2.7** - 404 回應包含 `error` 和 `accountId` 欄位

#### AC3: 查詢交易功能
- [ ] **AC3.1** - 回傳 3 筆交易記錄（陣列）
- [ ] **AC3.2** - 每筆交易包含 `date`, `amount`, `description`, `balance` 欄位
- [ ] **AC3.3** - 交易按時間倒序排列（最新在前）
- [ ] **AC3.4** - 第一筆交易：ATM 提款 -500
- [ ] **AC3.5** - 第二筆交易：薪資入帳 +3000
- [ ] **AC3.6** - 第三筆交易：信用卡繳費 -1200
- [ ] **AC3.7** - 不存在的帳號回傳 404 狀態碼

#### AC4: 資料精度
- [ ] **AC4.1** - 金額使用 `BigDecimal` 型別（避免浮點數誤差）
- [ ] **AC4.2** - 金額保留兩位小數
- [ ] **AC4.3** - 金額計算精確無誤差

#### AC5: 錯誤處理
- [ ] **AC5.1** - 帳戶不存在時回傳 404 Not Found
- [ ] **AC5.2** - 錯誤訊息為繁體中文
- [ ] **AC5.3** - 錯誤回應格式與 .NET 版本一致

### 非功能性驗收標準

#### AC6: 效能要求
- [ ] **AC6.1** - API 回應時間 < 100ms (P95)
- [ ] **AC6.2** - 支援並發請求 ≥ 100 TPS
- [ ] **AC6.3** - 記憶體使用 < 512MB

#### AC7: 程式碼品質
- [ ] **AC7.1** - 遵循 Java 命名慣例（camelCase）
- [ ] **AC7.2** - 使用 Lombok 減少樣板程式碼
- [ ] **AC7.3** - 程式碼註解完整（中文）
- [ ] **AC7.4** - 無編譯警告
- [ ] **AC7.5** - 通過 SonarQube 掃描（無 Critical/Blocker 問題）

#### AC8: 測試覆蓋率
- [ ] **AC8.1** - 單元測試覆蓋率 ≥ 80%
- [ ] **AC8.2** - Controller 層測試完整
- [ ] **AC8.3** - Service 層測試完整
- [ ] **AC8.4** - 整合測試涵蓋所有 API 端點

#### AC9: 文檔完整性
- [ ] **AC9.1** - README.md 包含啟動說明
- [ ] **AC9.2** - API 文檔完整（可使用 Swagger）
- [ ] **AC9.3** - 程式碼註解清晰
- [ ] **AC9.4** - 遷移對照文檔完整

#### AC10: 部署就緒
- [ ] **AC10.1** - 可使用 `mvn spring-boot:run` 啟動
- [ ] **AC10.2** - 可打包為 JAR 檔案
- [ ] **AC10.3** - 配置檔案外部化（application.yml）
- [ ] **AC10.4** - 日誌輸出正常

---

## 🎯 最佳化建議

### 1. 架構最佳化

#### 1.1 引入 Repository 層
雖然目前使用記憶體資料，但建議加入 Repository 層為未來資料庫整合做準備：

```java
@Repository
public class AccountRepository {
    private static final Map<String, BigDecimal> accounts = new HashMap<>();
    
    static {
        accounts.put("ACC001", new BigDecimal("50000.00"));
        accounts.put("ACC002", new BigDecimal("120000.00"));
        accounts.put("ACC003", new BigDecimal("8500.50"));
    }
    
    public Optional<BigDecimal> findBalanceByAccountId(String accountId) {
        return Optional.ofNullable(accounts.get(accountId));
    }
}
```

**優點：**
- 分離資料存取邏輯
- 易於未來切換到 JPA/MyBatis
- 符合單一職責原則

#### 1.2 使用 DTO 模式
明確區分內部模型和 API 回應：

```java
// 內部模型
@Entity
public class Account {
    private String accountId;
    private BigDecimal balance;
    private String currency;
}

// API 回應 DTO
@Data
public class BalanceResponseDto {
    private String accountId;
    private BigDecimal balance;
    private String currency;
}
```

### 2. 錯誤處理最佳化

#### 2.1 全域例外處理器
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotFound(AccountNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            "帳戶不存在",
            ex.getAccountId(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
            "系統錯誤",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
```

#### 2.2 自訂例外類別
```java
public class AccountNotFoundException extends RuntimeException {
    private final String accountId;
    
    public AccountNotFoundException(String accountId) {
        super("帳戶不存在: " + accountId);
        this.accountId = accountId;
    }
    
    public String getAccountId() {
        return accountId;
    }
}
```

### 3. 日誌最佳化

#### 3.1 結構化日誌
```java
@Slf4j
@RestController
@RequestMapping("/api/account")
public class AccountController {
    
    @GetMapping("/{accountId}/balance")
    public ResponseEntity<?> getBalance(@PathVariable String accountId) {
        log.info("查詢帳戶餘額請求 - accountId: {}", accountId);
        
        try {
            BigDecimal balance = service.getBalance(accountId);
            log.info("查詢成功 - accountId: {}, balance: {}", accountId, balance);
            return ResponseEntity.ok(new BalanceResponse(accountId, balance, "TWD"));
        } catch (AccountNotFoundException ex) {
            log.warn("帳戶不存在 - accountId: {}", accountId);
            throw ex;
        }
    }
}
```

### 4. 效能最佳化

#### 4.1 快取機制
```java
@Service
public class AccountService {
    
    @Cacheable(value = "balances", key = "#accountId")
    public BigDecimal getBalance(String accountId) {
        // 實際查詢邏輯
    }
}
```

#### 4.2 非同步處理（如需要）
```java
@Async
public CompletableFuture<List<Transaction>> getRecentTransactionsAsync(String accountId) {
    return CompletableFuture.completedFuture(getRecentTransactions(accountId));
}
```

### 5. 安全性最佳化

#### 5.1 輸入驗證
```java
@GetMapping("/{accountId}/balance")
public ResponseEntity<?> getBalance(
    @PathVariable 
    @Pattern(regexp = "^ACC\\d{3}$", message = "帳號格式錯誤") 
    String accountId) {
    // ...
}
```

#### 5.2 CORS 限制（生產環境）
```java
@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("https://trusted-domain.com")  // 限制來源
                        .allowedMethods("GET", "POST")
                        .allowedHeaders("*")
                        .maxAge(3600);
            }
        };
    }
}
```

### 6. 測試最佳化

#### 6.1 Controller 測試範例
```java
@WebMvcTest(AccountController.class)
class AccountControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private AccountService service;
    
    @Test
    void testGetBalance_Success() throws Exception {
        when(service.getBalance("ACC001")).thenReturn(new BigDecimal("50000.00"));
        when(service.accountExists("ACC001")).thenReturn(true);
        
        mockMvc.perform(get("/api/account/ACC001/balance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value("ACC001"))
                .andExpect(jsonPath("$.balance").value(50000.00))
                .andExpect(jsonPath("$.currency").value("TWD"));
    }
    
    @Test
    void testGetBalance_NotFound() throws Exception {
        when(service.getBalance("ACC999")).thenReturn(BigDecimal.ZERO);
        when(service.accountExists("ACC999")).thenReturn(false);
        
        mockMvc.perform(get("/api/account/ACC999/balance"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("帳戶不存在"));
    }
}
```

### 7. 文檔最佳化

#### 7.1 整合 Swagger/OpenAPI
```java
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("凱基銀行帳戶查詢 API")
                        .version("1.0")
                        .description("帳戶餘額與交易記錄查詢服務"));
    }
}
```

---

## 📝 實作檢查清單

### Phase 1: 專案建立
- [ ] 使用 Spring Initializr 建立專案
- [ ] 配置 Maven pom.xml
- [ ] 建立專案目錄結構
- [ ] 配置 application.yml

### Phase 2: 核心功能實作
- [ ] 實作 AccountApplication.java
- [ ] 實作 AccountController.java
- [ ] 實作 AccountService.java
- [ ] 實作 Transaction.java
- [ ] 實作 BalanceResponse.java
- [ ] 實作 CorsConfig.java

### Phase 3: 錯誤處理
- [ ] 實作 GlobalExceptionHandler
- [ ] 實作 AccountNotFoundException
- [ ] 實作 ErrorResponse DTO

### Phase 4: 測試
- [ ] 撰寫 AccountControllerTest
- [ ] 撰寫 AccountServiceTest
- [ ] 執行整合測試
- [ ] 驗證測試覆蓋率

### Phase 5: 文檔與部署
- [ ] 撰寫 README.md
- [ ] 整合 Swagger 文檔
- [ ] 本地測試驗證
- [ ] 打包 JAR 檔案

---

## 🧪 測試計畫

### 單元測試

#### AccountServiceTest
```java
@Test
void testGetBalance_ExistingAccount() {
    BigDecimal balance = service.getBalance("ACC001");
    assertEquals(new BigDecimal("50000.00"), balance);
}

@Test
void testGetBalance_NonExistingAccount() {
    BigDecimal balance = service.getBalance("ACC999");
    assertEquals(BigDecimal.ZERO, balance);
}

@Test
void testAccountExists_True() {
    assertTrue(service.accountExists("ACC001"));
}

@Test
void testAccountExists_False() {
    assertFalse(service.accountExists("ACC999"));
}
```

### 整合測試

#### API 端點測試
```bash
# 測試查詢餘額 - 成功
curl http://localhost:8080/api/account/ACC001/balance

# 預期回應
{
  "accountId": "ACC001",
  "balance": 50000.00,
  "currency": "TWD"
}

# 測試查詢餘額 - 失敗
curl http://localhost:8080/api/account/ACC999/balance

# 預期回應 (404)
{
  "error": "帳戶不存在",
  "accountId": "ACC999"
}

# 測試查詢交易
curl http://localhost:8080/api/account/ACC001/transactions

# 預期回應
[
  {
    "date": "2024-03-30T...",
    "amount": -500,
    "description": "ATM 提款",
    "balance": 50000.00
  },
  ...
]
```

---

## 📈 遷移時程規劃

| 階段 | 任務 | 預估時間 | 負責人 |
|------|------|---------|--------|
| Phase 1 | 專案建立與環境設定 | 0.5 天 | 開發團隊 |
| Phase 2 | 核心功能實作 | 1.5 天 | 開發團隊 |
| Phase 3 | 錯誤處理與最佳化 | 1 天 | 開發團隊 |
| Phase 4 | 測試與驗證 | 1 天 | QA 團隊 |
| Phase 5 | 文檔與部署準備 | 0.5 天 | 開發團隊 |
| **總計** | | **4.5 天** | |

---

## 🔍 風險評估與對策

### 風險 1: 金額精度問題
- **風險等級:** 高
- **影響:** 金額計算錯誤可能導致財務損失
- **對策:** 
  - 使用 `BigDecimal` 而非 `double`
  - 撰寫完整的精度測試
  - Code Review 重點檢查

### 風險 2: API 相容性
- **風險等級:** 中
- **影響:** 前端或其他系統無法正常呼叫
- **對策:**
  - 使用相同的路徑和參數名稱
  - 回應格式完全一致
  - 進行端對端測試

### 風險 3: 效能差異
- **風險等級:** 低
- **影響:** 回應時間可能不同
- **對策:**
  - 進行效能測試
  - 必要時加入快取機制
  - 監控生產環境效能

---

## 📚 參考資源

### 官方文檔
- [Spring Boot 官方文檔](https://spring.io/projects/spring-boot)
- [Spring Web MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
- [Java BigDecimal 最佳實踐](https://docs.oracle.com/javase/8/docs/api/java/math/BigDecimal.html)

### 遷移指南
- [.NET to Java Migration Guide](https://learn.microsoft.com/en-us/dotnet/architecture/modernize-desktop/example-migration)
- [ASP.NET Core to Spring Boot](https://spring.io/guides)

---

## ✅ 驗收檢查表

在完成遷移後，請確認以下所有項目：

### 功能驗收
- [ ] 所有 API 端點正常運作
- [ ] 回應格式與 .NET 版本完全一致
- [ ] 測試資料正確
- [ ] 錯誤處理正確

### 品質驗收
- [ ] 程式碼通過 Code Review
- [ ] 測試覆蓋率達標
- [ ] 無編譯警告
- [ ] 文檔完整

### 部署驗收
- [ ] 本地環境可正常啟動
- [ ] JAR 檔案可正常執行
- [ ] 配置檔案正確
- [ ] 日誌輸出正常

---

**文件狀態:** ✅ 已完成  
**最後更新:** 2026-03-31  
**審核者:** 待指定  
**核准者:** 待指定