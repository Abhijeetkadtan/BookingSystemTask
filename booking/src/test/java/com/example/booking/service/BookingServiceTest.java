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
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class BookingServiceTest {
    @Mock
    private SlotRepository slotRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private BookingService bookingService;
    private AppUser user;
    private Slot slot;

    BookingServiceTest() {
    }

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        this.user = new AppUser();
        this.user.setId(1L);
        this.user.setUsername("user1");
        this.slot = new Slot();
        this.slot.setId(1L);
        this.slot.setStatus(SlotStatus.AVAILABLE);
    }

    @Test
    void bookSlot_success() {
        Mockito.when(this.userRepository.findById(1L)).thenReturn(Optional.of(this.user));
        Mockito.when(this.slotRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(this.slot));
        Mockito.when(this.bookingRepository.save((Booking)Mockito.any())).thenAnswer((i) -> {
            return i.getArgument(0);
        });
        Booking booking = this.bookingService.bookSlot(1L, 1L, "user1");
        Assertions.assertEquals(BookingStatus.ACTIVE, booking.getStatus());
        Assertions.assertEquals(SlotStatus.BOOKED, this.slot.getStatus());
    }

    @Test
    void bookSlot_userMismatch() {
        Mockito.when(this.userRepository.findById(1L)).thenReturn(Optional.of(this.user));
        Assertions.assertThrows(UnauthorizedActionException.class, () -> {
            this.bookingService.bookSlot(1L, 1L, "wrongUser");
        });
    }

    @Test
    void bookSlot_slotNotFound() {
        Mockito.when(this.userRepository.findById(1L)).thenReturn(Optional.of(this.user));
        Mockito.when(this.slotRepository.findByIdForUpdate(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            this.bookingService.bookSlot(1L, 1L, "user1");
        });
    }

    @Test
    void bookSlot_alreadyBooked() {
        this.slot.setStatus(SlotStatus.BOOKED);
        Mockito.when(this.userRepository.findById(1L)).thenReturn(Optional.of(this.user));
        Mockito.when(this.slotRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(this.slot));
        Assertions.assertThrows(SlotAlreadyBookedException.class, () -> {
            this.bookingService.bookSlot(1L, 1L, "user1");
        });
    }

    @Test
    void cancelBooking_success() {
        Booking booking = new Booking();
        booking.setUserId(1L);
        booking.setSlot(this.slot);
        Mockito.when(this.userRepository.findById(1L)).thenReturn(Optional.of(this.user));
        Mockito.when(this.bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        this.bookingService.cancelBooking(1L, 1L, "user1");
        Assertions.assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        Assertions.assertEquals(SlotStatus.AVAILABLE, this.slot.getStatus());
    }

    @Test
    void cancelBooking_userMismatch() {
        Mockito.when(this.userRepository.findById(1L)).thenReturn(Optional.of(this.user));
        Assertions.assertThrows(UnauthorizedActionException.class, () -> {
            this.bookingService.cancelBooking(1L, 1L, "wrongUser");
        });
    }

    @Test
    void cancelBooking_bookingNotFound() {
        Mockito.when(this.userRepository.findById(1L)).thenReturn(Optional.of(this.user));
        Mockito.when(this.bookingRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            this.bookingService.cancelBooking(1L, 1L, "user1");
        });
    }
}
