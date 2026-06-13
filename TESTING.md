# PayFlow API Testing Guide

## Quick Start
1. Run the application: `./gradlew bootRun`
2. Open a new terminal in the same directory
3. Copy and run the curl commands below

---

## 1. REGISTER USERS

### Register User 1 (Alice)
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice Johnson",
    "email": "alice@example.com",
    "upiId": "alice@okaxis",
    "phoneNumber": "9876543210",
    "balance": 10000.0
  }'
```

Expected output:
```json
{
  "userId": 1,
  "name": "Alice Johnson",
  "email": "alice@example.com",
  "upiId": "alice@okaxis",
  "phoneNumber": "9876543210",
  "balance": 10000.0
}
```

### Register User 2 (Bob)
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bob Smith",
    "email": "bob@example.com",
    "upiId": "bob@okaxis",
    "phoneNumber": "9876543211",
    "balance": 5000.0
  }'
```

### Register User 3 (Charlie)
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Charlie Brown",
    "email": "charlie@example.com",
    "upiId": "charlie@okaxis",
    "phoneNumber": "9876543212",
    "balance": 3000.0
  }'
```

---

## 2. GET USER OPERATIONS

### Get User by ID
```bash
curl http://localhost:8080/users/1
```

### List All Users
```bash
curl http://localhost:8080/users
```

### Search User by Email
```bash
curl "http://localhost:8080/users/search/email?email=alice@example.com"
```

### Search User by UPI ID
```bash
curl "http://localhost:8080/users/upi/alice@okaxis"
```

### Search User by Phone Number
```bash
curl "http://localhost:8080/users/search/phone?phoneNumber=9876543210"
```

---

## 3. SEND MONEY (TRANSACTIONS)

### Transaction 1: Alice sends 1000 to Bob
```bash
curl -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "senderUpiId": "alice@okaxis",
    "receiverUpiId": "bob@okaxis",
    "amount": 1000.0,
    "note": "Lunch payment"
  }'
```

Expected balances after:
- Alice: 10000 - 1000 = 9000
- Bob: 5000 + 1000 = 6000

### Transaction 2: Bob sends 500 to Charlie
```bash
curl -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "senderUpiId": "bob@okaxis",
    "receiverUpiId": "charlie@okaxis",
    "amount": 500.0,
    "note": "Gift"
  }'
```

Expected balances after:
- Bob: 6000 - 500 = 5500
- Charlie: 3000 + 500 = 3500

### Transaction 3: Charlie sends 200 to Alice
```bash
curl -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "senderUpiId": "charlie@okaxis",
    "receiverUpiId": "alice@okaxis",
    "amount": 200.0,
    "note": "Debt repayment"
  }'
```

Expected balances after:
- Charlie: 3500 - 200 = 3300
- Alice: 9000 + 200 = 9200

---

## 4. GET TRANSACTION OPERATIONS

### Get All Transactions
```bash
curl http://localhost:8080/transactions
```

Should show 3 transactions

### Get Transaction by ID
```bash
curl http://localhost:8080/transactions/1
```

### Get All Transactions Sent by Alice
```bash
curl "http://localhost:8080/transactions/search/sender?senderUpiId=alice@okaxis"
```

### Get All Transactions Received by Bob
```bash
curl "http://localhost:8080/transactions/search/receiver?receiverUpiId=bob@okaxis"
```

### Get All Transactions Sent by Bob
```bash
curl "http://localhost:8080/transactions/search/sender?senderUpiId=bob@okaxis"
```

---

## 5. VERIFY FINAL BALANCES

### Check Alice's Final Balance
```bash
curl "http://localhost:8080/users/upi/alice@okaxis" | jq '.balance'
```

Expected: 9200.0

### Check Bob's Final Balance
```bash
curl "http://localhost:8080/users/upi/bob@okaxis" | jq '.balance'
```

Expected: 5500.0

### Check Charlie's Final Balance
```bash
curl "http://localhost:8080/users/upi/charlie@okaxis" | jq '.balance'
```

Expected: 3300.0

---

## 6. ERROR SCENARIOS (Testing Validations)

### Error 1: Insufficient Balance
```bash
curl -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "senderUpiId": "alice@okaxis",
    "receiverUpiId": "bob@okaxis",
    "amount": 50000.0,
    "note": "Should fail"
  }'
```

Expected error: `Error: Insufficient balance. Available: 9200.0`

### Error 2: Sender Not Found
```bash
curl -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "senderUpiId": "invalid@okaxis",
    "receiverUpiId": "bob@okaxis",
    "amount": 100.0,
    "note": "Invalid sender"
  }'
```

Expected error: `Error: Sender not found with UPI: invalid@okaxis`

### Error 3: Receiver Not Found
```bash
curl -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "senderUpiId": "alice@okaxis",
    "receiverUpiId": "invalid@okaxis",
    "amount": 100.0,
    "note": "Invalid receiver"
  }'
```

Expected error: `Error: Receiver not found with UPI: invalid@okaxis`

### Error 4: Missing Required Fields
```bash
curl -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "senderUpiId": "alice@okaxis",
    "amount": 100.0
  }'
```

Expected error: `Missing required fields: senderUpiId, receiverUpiId, amount`

---

## 7. VIEWING DATABASE (Optional - Using H2 Console)

1. Open browser: http://localhost:8080/h2-console
2. Connection settings:
   - JDBC URL: `jdbc:h2:mem:payflowdb`
   - Username: `sa`
   - Password: (leave empty)
3. Click "Connect"
4. Run SQL queries:

### View All Users
```sql
SELECT * FROM users;
```

### View All Transactions
```sql
SELECT * FROM transactions;
```

### Check Balances
```sql
SELECT user_id, name, email, phone_number, balance FROM users;
```

---

## 8. KEY ASSERTIONS TO VERIFY

After running all tests:

✅ 3 users registered successfully
✅ 3 transactions created successfully
✅ Balances updated correctly:
   - Alice: 9200.0
   - Bob: 5500.0
   - Charlie: 3300.0
✅ All custom queries work (search by phone, sender, receiver)
✅ Error validations work (insufficient balance, user not found, missing fields)
✅ Database queries show correct state

---

## 9. CLEAN UP

To restart testing:
1. Stop the app: `Ctrl+C`
2. Run again: `./gradlew bootRun`
3. Database resets automatically (in-memory H2)
4. Repeat tests from step 1

---

## 10. PRETTY PRINT JSON (Optional)

If you have `jq` installed, format responses:

```bash
curl http://localhost:8080/users | jq '.'
```

For Windows (without jq):
```bash
curl http://localhost:8080/users | powershell -Command Add-Content -Encoding UTF8 | ConvertFrom-Json
```

---

## Summary of Custom Queries Tested

✅ Find user by UPI ID (Custom @Query in UserRepository)
✅ Find user by phone number (Custom @Query in UserRepository)
✅ Find transactions by sender UPI ID (Custom @Query in TransactionRepository)
✅ Find transactions by receiver UPI ID (Custom @Query in TransactionRepository)
