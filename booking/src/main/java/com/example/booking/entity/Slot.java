package com.example.booking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.Generated;

@Entity
@Table(
        name = "slots",
        uniqueConstraints = {@UniqueConstraint(
                columnNames = {"start_time", "end_time"}
        )}
)
public class Slot {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    @Column(
            name = "start_time",
            nullable = false
    )
    private LocalDateTime startTime;
    @Column(
            name = "end_time",
            nullable = false
    )
    private LocalDateTime endTime;
    @Enumerated(EnumType.STRING)
    private SlotStatus status;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    @Generated
    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    @Generated
    public SlotStatus getStatus() {
        return this.status;
    }

    @Generated
    public void setId(final Long id) {
        this.id = id;
    }

    @Generated
    public void setStartTime(final LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Generated
    public void setEndTime(final LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Generated
    public void setStatus(final SlotStatus status) {
        this.status = status;
    }

    @Generated
    public Slot() {
    }
}
