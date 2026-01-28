package com.example.booking.controller;

import com.example.booking.entity.Slot;
import com.example.booking.entity.SlotStatus;
import com.example.booking.repository.SlotRepository;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/slots"})
public class SlotController {
    private final SlotRepository slotRepository;

    public SlotController(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Slot createSlot(@RequestBody Slot slot) {
        boolean exists = this.slotRepository.existsByStartTimeAndEndTime(slot.getStartTime(), slot.getEndTime());
        if (exists) {
            throw new IllegalStateException("Slot already exists");
        } else {
            slot.setStatus(SlotStatus.AVAILABLE);
            return (Slot)this.slotRepository.save(slot);
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<Slot> getAllSlots() {
        return this.slotRepository.findAll();
    }
}
