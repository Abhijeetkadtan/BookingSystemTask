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

