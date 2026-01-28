package com.example.booking.service;

import com.example.booking.entity.AppUser;
import com.example.booking.entity.Booking;
import com.example.booking.entity.BookingStatus;
import com.example.booking.entity.Slot;
import com.example.booking.entity.SlotStatus;
import com.example.booking.exception.ResourceNotFoundException;
import com.example.booking.exception.SlotAlreadyBookedException;
import com.example.booking.exception.UnauthorizedActionException;
import com.example.booking.repository.BookingRepository;
import com.example.booking.repository.SlotRepository;
import com.example.booking.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    private final SlotRepository slotRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public BookingService(SlotRepository slotRepository, BookingRepository bookingRepository, UserRepository userRepository) {
        this.slotRepository = slotRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Booking bookSlot(Long slotId, Long userId, String jwtUsername) {
        AppUser user = (AppUser)this.userRepository.findById(userId).orElseThrow(() -> {
            return new ResourceNotFoundException("User not found");
        });
        if (!user.getUsername().equals(jwtUsername)) {
            throw new UnauthorizedActionException("userId does not belong to logged-in user");
        } else {
            Slot slot = (Slot)this.slotRepository.findByIdForUpdate(slotId).orElseThrow(() -> {
                return new ResourceNotFoundException("Slot not found");
            });
            if (slot.getStatus() == SlotStatus.BOOKED) {
                throw new SlotAlreadyBookedException("Slot already booked");
            } else {
                slot.setStatus(SlotStatus.BOOKED);
                Booking booking = new Booking();
                booking.setSlot(slot);
                booking.setUserId(userId);
                booking.setStatus(BookingStatus.ACTIVE);
                booking.setCreatedAt(LocalDateTime.now());
                this.slotRepository.save(slot);
                return (Booking)this.bookingRepository.save(booking);
            }
        }
    }

    @Transactional
    public void cancelBooking(Long bookingId, Long userId, String jwtUsername) {
        AppUser user = (AppUser)this.userRepository.findById(userId).orElseThrow(() -> {
            return new ResourceNotFoundException("User not found");
        });
        if (!user.getUsername().equals(jwtUsername)) {
            throw new UnauthorizedActionException("userId does not belong to logged-in user");
        } else {
            Booking booking = (Booking)this.bookingRepository.findById(bookingId).orElseThrow(() -> {
                return new ResourceNotFoundException("Booking not found");
            });
            if (!booking.getUserId().equals(userId)) {
                throw new UnauthorizedActionException("Unauthorized");
            } else {
                booking.setStatus(BookingStatus.CANCELLED);
                booking.getSlot().setStatus(SlotStatus.AVAILABLE);
            }
        }
    }

    @Transactional
    public void adminCancelBooking(Long bookingId) {
        Booking booking = (Booking)this.bookingRepository.findById(bookingId).orElseThrow(() -> {
            return new ResourceNotFoundException("Booking not found");
        });
        booking.setStatus(BookingStatus.CANCELLED);
        booking.getSlot().setStatus(SlotStatus.AVAILABLE);
    }
}
