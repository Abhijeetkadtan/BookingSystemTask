# 🏥 Concurrency-Safe Slot Booking System

A Spring Boot backend system that allows users to book and cancel time slots while ensuring **strict concurrency control** so that a slot can **never be double-booked**, even under heavy parallel requests.

This project demonstrates real-world backend engineering concepts:

- Database Transactions
- Concurrency Control
- Race Condition Prevention
- Database Locking
- Spring Transaction Management
- Secure API Design

---

## 🎯 Core Problem Solved

When multiple users attempt to book the same slot simultaneously:

✔ Only **one booking must succeed**  
✔ Data must remain **consistent**  
✔ No partial updates  
✔ System must remain correct even after restart  

---

# 🔒 Locking Strategy Used

### ✅ **Pessimistic Locking (Database-Level)**

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT s FROM Slot s WHERE s.id = :id")
Optional<Slot> findByIdForUpdate(Long id);

```
--- 
# 🔁 Transaction Boundaries

### All booking operations execute inside a transaction.**

```java
@Transactional
public Booking bookSlot(Long slotId, Long userId, String jwtUsername)
```
--- 

# ⚠️ How Race Conditions Are Prevented

### Race condition example:

Two users try to book the same slot at the exact same time.

Without locking:

Both requests see slot AVAILABLE → double booking ❌

With our design:
| Step | Thread A                   | Thread B                       |
|------|----------------------------|--------------------------------|
| 1    | Locks Slot row             | Waits                          |
| 2    | Checks status = AVAILABLE  | Still Waiting                  |
| 3    | Set Status = BOOKED        | Waiting                        |
| 4    | Commits                    | Lock Released                  |
| 5    | -                          | Reads Status =BOOKED -> Fails  |

Result: Only one booking succeeds.

---

# 👥 Example Concurrent Booking Scenario
### Scenario:

Two users try to book Slot ID = 1 simultaneously.
| Time  | User A             | User B                               |
|-------|--------------------|--------------------------------------|
| T1    | Starts Transaction | Starts Transaction                   |
| T2    | Acquires Lock      | Waiting for lock                     |
| T3    | Books Slot         | Waiting                              |
| T4    | Commits            | Lock Released                        |
| T5    | -                  | Reads Slot =BOOKED                   |
| T6    | -                  | Throws slot already booked(Conflict) |

---

# 🚀 How to Run the Application

## 1️⃣ Prerequisites

- Java 17+
- Maven
- Postman (or curl)

---

## 2️⃣ Build Project

```bash
mvn clean install
```
## 3️⃣ Start Application
```
mvn spring-boot:run
```
### App runs at:
```
http://localhost:8080 
```
## 4️⃣ H2 Database Console

Open:
```
http://localhost:8080/h2-console
```
Field	Value
JDBC URL	jdbc:h2:file:./data/bookingdb
Username	sa
Password	(empty)

## 🔐 Authentication Flow (JWT)

All secured APIs require JWT token.

### 🟢 Step 1 — Register User
Endpoint
```
POST /auth/register
```

Body
```
{
  "username": "user1",
  "password": "pass123",
  "role": "USER"
}
```
Register Admin
```
{
  "username": "admin1",
  "password": "admin123",
  "role": "ADMIN"
}
```
### 🔵 Step 2 — Login
Endpoint
```
POST /auth/login
```
Body
```
{
  "username": "user1",
  "password": "pass123"
}
```
Response
```
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```
### 📌 Copy this token.

### 🔑 Step 3 — Use Token

Add header in all requests:
```
Authorization: Bearer <your_token_here>
```
## 👨‍⚕️ ADMIN FLOW

### 🟣 Create Slot
Endpoint
```
POST /slots
```
Role: ADMIN

Body
```
{
  "startTime": "2026-01-30T10:00:00",
  "endTime": "2026-01-30T11:00:00"
}
```
## 👤 USER FLOW

### 🟡 View All Slots
```
GET /slots
```
Response example:
```
[
  {
    "id": 1,
    "startTime": "2026-01-30T10:00",
    "endTime": "2026-01-30T11:00",
    "status": "AVAILABLE"
  }
]
```
### 🔴 Book Slot
```
POST /bookings?slotId=1&userId=1
```
Success Response
```
{
  "id": 5,
  "userId": 1,
  "status": "ACTIVE"
}
```
### ❌ Try Double Booking
If another user tries:
```
POST /bookings?slotId=1&userId=2
```
Response:
```
{
  "status": 409,
  "message": "Slot already booked"
}
```
###🟢 Cancel Booking (User)
```
POST /bookings/5/cancel?userId=1
```
Booking status becomes CANCELLED and slot becomes AVAILABLE.

###🔴 Admin Cancel Any Booking
```
POST /admin/bookings/5/cancel
```


