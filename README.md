# PayFlow REST API

A simplified payment system REST API built with **Spring Boot 3.3** that handles user registration, balance management, and money transfers between users.

---

## 🚀 How to Run the Application

### Prerequisites
- Java 23 installed
- Gradle (or use the included gradle wrapper)

### Build the Project
```bash
cd payflow
./gradlew build
```

### Run the Application
```bash
./gradlew bootRun
```

The application will start on **http://localhost:8080**

### Access H2 Console
Navigate to **http://localhost:8080/h2-console** with credentials:
- JDBC URL: `jdbc:h2:mem:payflowdb`
- Username: `sa`
- Password: (leave blank)

---

## 🏗️ Architecture & Layer Descriptions

PayFlow follows a **4-layer architecture** for clean separation of concerns:

### 1. **Entity Layer** (`entity/`)
- **User.java**: Represents a user with fields: userId, name, email, upiId, phoneNumber, balance
- **Transaction.java**: Represents a money transfer with fields: transactionId, senderUpiId, receiverUpiId, amount, note
- **Purpose**: Define the data model and map to database tables using JPA annotations
- **Technology**: JPA @Entity, @Column, @Id, @GeneratedValue

### 2. **Repository Layer** (`repository/`)
- **UserRepository**: Extends JpaRepository to provide CRUD operations on User entity
- **TransactionRepository**: Extends JpaRepository to provide CRUD operations on Transaction entity
- **Purpose**: Abstract database queries and provide a clean interface for data access
- **Key Method**: `findByUpiId(String upiId)` - Custom derived query to find users by UPI ID
- **Technology**: Spring Data JPA, derived query methods, @Query annotations

### 3. **Service Layer** (`service/`)
- **UserService**: Contains business logic for user operations (register, search, retrieve)
- **TransactionService**: Contains business logic for transaction processing (send money, retrieve transaction history)
- **Purpose**: Implement business rules, validation, and coordinate between controller and repository
- **Technology**: @Service, @Autowired, business logic methods

### 4. **Controller Layer** (`controller/`)
- **UserController**: REST endpoints for user management (POST /users, GET /users, etc.)
- **TransactionController**: REST endpoints for transactions (POST /transactions, GET /transactions, etc.)
- **Purpose**: Handle HTTP requests/responses and route to appropriate service methods
- **Technology**: @RestController, @RequestMapping, @PostMapping, @GetMapping

---

## ⚙️ Spring Boot Features in PayFlow

PayFlow demonstrates **three core Spring Boot features**:

### 1. **Auto-Configuration**
Spring Boot automatically configures beans and dependencies based on classpath entries.

**How it appears in PayFlow:**
- We added `spring-boot-starter-data-jpa` dependency, and Spring Boot automatically:
  - Created a JPA EntityManager and SessionFactory
  - Configured Hibernate as the JPA provider
  - Set up the database connection pool
  - Enabled transaction management
- We simply added `spring.datasource.url=jdbc:h2:mem:payflowdb` in `application.properties`, and Spring Boot auto-created the H2 datasource without manual bean definitions

**Benefit:** Eliminates hundreds of lines of XML configuration files and Java config classes.

### 2. **Embedded Server**
Spring Boot includes an embedded Tomcat server, so you don't need to deploy to an external application server.

**How it appears in PayFlow:**
- When you run `./gradlew bootRun`, the embedded Tomcat starts automatically on port 8080
- The application listens for HTTP requests immediately without manual server setup
- No need to download/install Tomcat separately or create a WAR file

**Benefit:** Development and testing become instant; you don't waste time on server configuration.

### 3. **Production-Ready Defaults**
Spring Boot provides sensible default configurations optimized for production use.

**How it appears in PayFlow:**
- Default error handling with proper HTTP status codes (400, 404, 500)
- Built-in health checks (if actuator is added)
- Graceful shutdown handling
- Proper logging configuration (SLF4J + Logback)
- H2 console automatically enabled for development (can be disabled in production via `spring.h2.console.enabled=false`)

**Benefit:** Your application is production-ready from day one with minimal configuration.

---

## 🛠️ Tech Stack

- **Framework**: Spring Boot 3.3
- **Build Tool**: Gradle (Kotlin DSL)
- **Database**: H2 (in-memory, auto-configured)
- **ORM**: Spring Data JPA
- **Java**: 23
- **Utilities**: Lombok (reduces boilerplate code)

---

## 📁 Project Structure

```
payflow/
├── src/main/java/com/airtribe/payflow/
│   ├── entity/                 # JPA entities
│   │   ├── User.java           # User entity with userId, name, email, balance, phoneNumber
│   │   └── Transaction.java    # Transaction entity with transactionId, senderUPI_id, receiverUPI_id, amount, note
│   ├── repository/             # Data access layer
│   │   ├── UserRepository.java          # User CRUD + custom queries
│   │   └── TransactionRepository.java   # Transaction CRUD + custom queries
│   ├── service/                # Business logic
│   │   ├── UserService.java             # User operations
│   │   └── TransactionService.java      # Transaction operations & balance updates
│   ├── controller/             # REST endpoints
│   │   ├── UserController.java          # User endpoints
│   │   └── TransactionController.java   # Transaction endpoints
│   └── PayflowApplication.java # Spring Boot main class
├── src/main/resources/
│   └── application.properties  # Database & server configuration
├── build.gradle.kts            # Gradle build configuration
└── README.md                   # This file
```

---

## 🚀 Getting Started

### 1. Build the Project
```bash
cd payflow
./gradlew build
```

### 2. Run the Application
```bash
./gradlew bootRun
```

The server starts on **http://localhost:8080**

### 3. Access H2 Console (Optional)
Navigate to: **http://localhost:8080/h2-console**
- **JDBC URL**: `jdbc:h2:mem:payflowdb`
- **Username**: `sa`
- **Password**: (leave blank)

---

## 📡 API Endpoints

### **User Endpoints**

#### 1. Register a User
```bash
POST /users
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "upiId": "john@okaxis",
  "phoneNumber": "9876543210",
  "balance": 5000.0
}
```

**Response (201 Created)**:
```json
{
  "userId": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "upiId": "john@okaxis",
  "phoneNumber": "9876543210",
  "balance": 5000.0
}
```

#### 2. Get User by ID
```bash
GET /users/{id}
```

**Response (200 OK)**:
```json
{
  "userId": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "upiId": "john@okaxis",
  "phoneNumber": "9876543210",
  "balance": 5000.0
}
```

#### 3. List All Users
```bash
GET /users
```

**Response (200 OK)**:
```json
[
  {
    "userId": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "upiId": "john@okaxis",
    "phoneNumber": "9876543210",
    "balance": 5000.0
  },
  {
    "userId": 2,
    "name": "Jane Smith",
    "email": "jane@example.com",
    "upiId": "jane@okaxis",
    "phoneNumber": "9876543211",
    "balance": 3000.0
  }
]
```

#### 4. Search User by Email
```bash
GET /users/search/email?email=john@example.com
```

#### 5. Search User by UPI ID
```bash
GET /users/upi/john@okaxis
```

#### 6. Search User by Phone
```bash
GET /users/search/phone?phoneNumber=9876543210
```

---

### **Transaction Endpoints**

#### 1. Send Money
```bash
POST /transactions
Content-Type: application/json

{
  "senderUpiId": "john@okaxis",
  "receiverUpiId": "jane@okaxis",
  "amount": 500.0,
  "note": "Payment for lunch"
}
```

**Response (201 Created)**:
```json
{
  "transactionId": 1,
  "senderUpiId": "john@okaxis",
  "receiverUpiId": "jane@okaxis",
  "amount": 500.0,
  "note": "Payment for lunch"
}
```

**Validation**:
- Sender and receiver must be registered users
- Sender must have sufficient balance
- All fields required: senderUpiId, receiverUpiId, amount

#### 2. Get All Transactions
```bash
GET /transactions
```

#### 3. Get Transaction by ID
```bash
GET /transactions/{id}
```

#### 4. Search Transactions by Sender
```bash
GET /transactions/search/sender?senderUpiId=john@okaxis
```

#### 5. Search Transactions by Receiver
```bash
GET /transactions/search/receiver?receiverUpiId=jane@okaxis
```

---

## 🧪 Testing with cURL

### Register Users
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice",
    "email": "alice@example.com",
    "upiId": "alice@okaxis",
    "phoneNumber": "9111111111",
    "balance": 10000.0
  }'

curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bob",
    "email": "bob@example.com",
    "upiId": "bob@okaxis",
    "phoneNumber": "9222222222",
    "balance": 5000.0
  }'
```

### Get All Users
```bash
curl http://localhost:8080/users
```

### Search User by UPI ID
```bash
curl "http://localhost:8080/users/upi/alice@okaxis"
```

### Send Money (Transaction)
```bash
curl -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "senderUpiId": "alice@okaxis",
    "receiverUpiId": "bob@okaxis",
    "amount": 1000.0,
    "note": "Loan repayment"
  }'
```

### Get All Transactions
```bash
curl http://localhost:8080/transactions
```

### Search User by Phone
```bash
curl "http://localhost:8080/users/search/phone?phoneNumber=9111111111"
```

### Get Transactions by Sender
```bash
curl "http://localhost:8080/transactions/search/sender?senderUpiId=alice@okaxis"
```

---

## 📝 Database Schema

### Users Table
| Column | Type | Constraints |
|--------|------|-------------|
| user_id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| name | VARCHAR(255) | NOT NULL |
| email | VARCHAR(255) | NOT NULL, UNIQUE |
| upi_id | VARCHAR(255) | NOT NULL, UNIQUE |
| phone_number | VARCHAR(255) | UNIQUE |
| balance | DOUBLE | NOT NULL |

### Transactions Table
| Column | Type | Constraints |
|--------|------|-------------|
| transaction_id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| sender_upi_id | VARCHAR(255) | NOT NULL |
| receiver_upi_id | VARCHAR(255) | NOT NULL |
| amount | DOUBLE | NOT NULL |
| note | VARCHAR(255) | NULL |

---

## 📚 Custom Query Methods (Task 3 & Task 6)

### Derived Query Method: findByUpiId()

In **UserRepository**, we have:
```java
Optional<User> findByUpiId(String upiId);
```

**SQL JPA Generates:**
```sql
SELECT * FROM USER WHERE UPI_ID = ?
```

**How JPA Derives It from Method Name:**
Spring Data JPA uses a parser that interprets method names following specific conventions:
- `findBy` = SELECT query
- `UpiId` = WHERE clause on the field `upiId`
- The field name is converted to UPPER_CASE with underscores for the column name: `upi_id`

The method signature tells JPA: "Find and return an Optional User matching the given UpiId". Spring generates the JPQL/SQL automatically without us writing a @Query annotation.

**What the ? Placeholder Means:**
The `?` is a **prepared statement parameter placeholder** that represents the value passed to the method (`String upiId`). Using prepared statements prevents SQL injection attacks because the database driver safely escapes user input before substituting it into the query. The actual value is bound to this placeholder at runtime.

---

## 🔄 Custom Query Approaches Comparison

There are **three ways** to write custom queries in Spring Data JPA. Here's how they compare:

### 1. **Derived Query Methods** (Using Method Names)
**Example:**
```java
Optional<User> findByUpiId(String upiId);
List<User> findByBalanceGreaterThan(Double amount);
```

**Pros:**
- Simple and readable for basic queries
- No SQL/JPQL code needed
- JPA auto-generates the query from method name

**Cons:**
- Limited to simple conditions (AND, OR, GreaterThan, etc.)
- Method names become verbose for complex queries
- Not suitable for complex business logic

**When to Use:** Simple single-field or two-field lookups

---

### 2. **@Query with JPQL** (Type-Safe Custom Queries)
**Example:**
```java
@Query("SELECT u FROM User u WHERE u.balance > :amount")
List<User> findUsersWithBalanceAbove(@Param("amount") Double amount);

@Query("SELECT u FROM User u WHERE u.upiId = :upiId AND u.balance < :maxBalance")
List<User> findActiveUsersBelow(@Param("upiId") String upiId, @Param("maxBalance") Double maxBalance);
```

**Pros:**
- Type-safe (JPA validates field and entity names at compile time)
- Supports complex queries with multiple conditions
- Refactoring-friendly (IDE auto-updates when entity fields change)
- Database-agnostic (works with any database)
- Uses JPQL (object-query language, not database-specific SQL)

**Cons:**
- Requires manual query writing
- More verbose than derived methods

**When to Use:** Complex multi-condition queries, OR operations, joins

---

### 3. **Native SQL Queries** (Database-Specific)
**Example:**
```java
@Query(value = "SELECT * FROM USER WHERE UPI_ID = ? AND BALANCE > ?", nativeQuery = true)
List<User> findUsersNative(String upiId, Double minBalance);
```

**Pros:**
- Full SQL power and optimization
- Can use database-specific features (window functions, CTEs, etc.)

**Cons:**
- **NOT type-safe** - If you rename a column in the User entity, this native query breaks silently
- **SQL injection risks** - Parameters must be carefully handled
- **Database-specific** - If you migrate from H2 to PostgreSQL, syntax may break
- **Hard to refactor** - IDE can't auto-update column names
- Violates ORM principles and defeats the purpose of using JPA

**When to Use:** Rarely. Only when you absolutely need database-specific SQL for performance reasons, and the query is stable.

---

### Why Native Queries Are Least Preferred:

**Native SQL queries are the least preferred approach** for several critical reasons:

1. **Loss of Type Safety**: JPQL queries are validated against your entity model. Native SQL bypasses this entirely. If you rename a User field, the native query still references the old column name and fails at runtime.

2. **SQL Injection Vulnerability**: While you can use parameterized native queries, the risk of injection is higher than JPQL, which the ORM layer validates.

3. **Database Portability**: If you ever need to switch from H2 to PostgreSQL or MySQL, your native SQL queries might use syntax specific to H2 (e.g., `UCASE()` vs `UPPER()`), requiring rewriting.

4. **Breaks ORM Abstraction**: The whole point of using an ORM is to abstract database details. Native SQL defeats this purpose.

5. **Harder Maintenance**: Your IDE can't refactor native SQL column names automatically when your entity fields change.

**Best Practice:** Use derived query methods for simple lookups, and @Query (JPQL) for complex queries. Only use native SQL when JPQL cannot express your business logic and you need raw database-specific performance optimization.

---

### UserService
- `registerUser()` - Creates new user with default balance if not provided
- `getUserById()` - Retrieves user by ID
- `getUserByEmail()` - Custom query to find by email
- `getUserByPhoneNumber()` - Custom @Query to find by phone
- `getAllUsers()` - Lists all users
- `updateUserBalance()` - Updates user balance

### TransactionService
- `sendMoney()` - Core transaction logic:
  1. Validates sender and receiver exist
  2. Checks sender has sufficient balance
  3. Deducts from sender, credits receiver
  4. Persists transaction record
- `getTransactionById()` - Finds transaction by ID
- `getTransactionsBySender()` - Custom query
- `getTransactionsByReceiver()` - Custom query

### Custom Queries
- **UserRepository**: `@Query("SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber")`
- **TransactionRepository**: Custom queries for sender and receiver lookup

---

## ⚙️ Configuration

### application.properties
```properties
# H2 Database
spring.datasource.url=jdbc:h2:mem:payflowdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa

# JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop  # Creates schema on startup, drops on shutdown
spring.jpa.show-sql=false

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Server
server.port=8080
```

---

## ✅ Assumptions & Constraints

1. **In-Memory Database**: All data is lost when the app stops (by design - `create-drop` mode)
2. **Phone as UPI**: The system uses phone numbers as UPI IDs for transactions
3. **Opening Balance**: If not provided during registration, balance defaults to 0
4. **No Balance Validation on Update**: Balance can be set to any value via internal method
5. **Transaction Note is Optional**: Can be null
6. **No Authentication**: This API has no security layer (add Spring Security in production)
7. **No Email Validation**: Email format is not validated

---

## 🐛 Error Handling

| Scenario | Status Code | Response |
|----------|------------|----------|
| Invalid request body | 400 | `Error: [message]` |
| User not found | 404 | `User not found with ID: [id]` |
| Insufficient balance | 400 | `Error: Insufficient balance. Available: [amount]` |
| Sender/receiver not found | 400 | `Error: Sender/Receiver not found with UPI: [upi]` |
| Missing required fields | 400 | `Error: Missing required fields: ...` |
| Internal server error | 500 | `Error: [exception message]` |

---

## 🛑 Stopping the Application

Press `Ctrl+C` in your terminal where `gradlew bootRun` is running.

---

## 📚 Additional Commands

```bash
# Clean build
./gradlew clean

# Run tests
./gradlew test

# Run with debugging
./gradlew bootRun --debug

# Build without running
./gradlew assemble
```

---


