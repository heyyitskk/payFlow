# PayFlow REST API

A simplified payment system REST API built with **Spring Boot 3.3** that handles user registration, balance management, and money transfers between users.

---

## 📋 Features

✅ **User Management**
- Register new users with name, email, phone, and opening balance
- Search users by ID, email, or phone number
- List all registered users
- Automatic balance initialization (default: 0 if not provided)

✅ **Transaction Management**
- Send money from one user to another using phone UPI
- Automatic balance updates (debit sender, credit receiver)
- Track all transactions with notes
- Search transactions by sender or receiver

✅ **Database**
- H2 in-memory database (auto-configured)
- Automatic schema creation on startup
- Accessible via H2 Console at `/h2-console`

---

## 🛠️ Tech Stack

- **Framework**: Spring Boot 3.3
- **Build Tool**: Gradle
- **Database**: H2 (in-memory)
- **ORM**: Spring Data JPA
- **Java**: 23 (configured in `build.gradle.kts`)
- **Utilities**: Lombok (reduces boilerplate)

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
    "phoneNumber": "9876543210",
    "balance": 5000.0
  },
  {
    "userId": 2,
    "name": "Jane Smith",
    "email": "jane@example.com",
    "phoneNumber": "9876543211",
    "balance": 3000.0
  }
]
```

#### 4. Search User by Email
```bash
GET /users/search/email?email=john@example.com
```

#### 5. Search User by Phone
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
  "senderUPI_id": "9876543210",
  "receiverUPI_id": "9876543211",
  "amount": 500.0,
  "note": "Payment for lunch"
}
```

**Response (201 Created)**:
```json
{
  "transactionId": 1,
  "senderUPI_id": "9876543210",
  "receiverUPI_id": "9876543211",
  "amount": 500.0,
  "note": "Payment for lunch"
}
```

**Validation**:
- Sender and receiver must be registered users
- Sender must have sufficient balance
- All fields required: senderUPI_id, receiverUPI_id, amount

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
GET /transactions/search/sender?senderUPI=9876543210
```

#### 5. Search Transactions by Receiver
```bash
GET /transactions/search/receiver?receiverUPI=9876543211
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
    "phoneNumber": "9111111111",
    "balance": 10000.0
  }'

curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bob",
    "email": "bob@example.com",
    "phoneNumber": "9222222222",
    "balance": 5000.0
  }'
```

### Get All Users
```bash
curl http://localhost:8080/users
```

### Send Money (Transaction)
```bash
curl -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "senderUPI_id": "9111111111",
    "receiverUPI_id": "9222222222",
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
curl "http://localhost:8080/transactions/search/sender?senderUPI=9111111111"
```

---

## 📝 Database Schema

### Users Table
| Column | Type | Constraints |
|--------|------|-------------|
| user_id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| name | VARCHAR(255) | NOT NULL |
| email | VARCHAR(255) | NOT NULL, UNIQUE |
| balance | DOUBLE | NOT NULL |
| phone_number | VARCHAR(255) | UNIQUE |

### Transactions Table
| Column | Type | Constraints |
|--------|------|-------------|
| transaction_id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| sender_upi_id | VARCHAR(255) | NOT NULL |
| receiver_upi_id | VARCHAR(255) | NOT NULL |
| amount | DOUBLE | NOT NULL |
| note | VARCHAR(255) | NULL |

---

## 🔍 Key Implementation Details

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


