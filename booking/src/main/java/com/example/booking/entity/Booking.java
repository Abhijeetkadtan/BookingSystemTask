package com.example.booking.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.Generated;

@Entity
@Table(
        name = "bookings"
)
public class Booking {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "slot_id", nullable = false)
    private Slot slot;

    private Long userId;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    private LocalDateTime createdAt;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public Slot getSlot() {
        return this.slot;
    }

    @Generated
    public Long getUserId() {
        return this.userId;
    }

    @Generated
    public BookingStatus getStatus() {
        return this.status;
    }

    @Generated
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Generated
    public void setId(final Long id) {
        this.id = id;
    }

    @Generated
    public void setSlot(final Slot slot) {
        this.slot = slot;
    }

    @Generated
    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    @Generated
    public void setStatus(final BookingStatus status) {
        this.status = status;
    }

    @Generated
    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Generated
    public Booking() {
    }
}
