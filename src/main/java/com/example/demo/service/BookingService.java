package com.example.demo.service;

import com.example.demo.entity.Booking;
import com.example.demo.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    // Check overlap prima di creare booking
    public boolean hasOverlap(Booking newBooking) {
        if (newBooking.getProperty() == null || newBooking.getProperty().getId() == null) {
            return true;  // Property obbligatoria
        }
        if (newBooking.getStartDate() == null || newBooking.getEndDate() == null ||
            newBooking.getStartDate().isAfter(newBooking.getEndDate())) {
            return true;  // Date invalide
        }
        Long propertyId = newBooking.getProperty().getId();
        List<Booking> overlaps = bookingRepository.findOverlappingBookings(
                propertyId, newBooking.getStartDate(), newBooking.getEndDate());
        return overlaps.stream().anyMatch(b -> !b.getId().equals(newBooking.getId()));
    }

    // Verifica disponibilità per range (usa in controller separato se serve)
    public boolean isAvailable(Long propertyId, LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            return false;
        }
        List<Booking> overlaps = bookingRepository.findOverlappingBookings(propertyId, startDate, endDate);
        return overlaps.isEmpty();
    }

    // Calcola occupancy rate (opzionale, per stats TorrePali)
    public double getOccupancyRate(Long propertyId, LocalDate month) {
        LocalDate start = month.withDayOfMonth(1);
        LocalDate end = start.plusMonths(1).minusDays(1);
        List<Booking> bookings = bookingRepository.findOverlappingBookings(propertyId, start, end);
        long occupiedDays = bookings.stream()
                .mapToLong(b -> java.time.temporal.ChronoUnit.DAYS.between(b.getStartDate(), b.getEndDate()))
                .sum();
        return (double) occupiedDays / start.until(end, java.time.temporal.ChronoUnit.DAYS);
    }
}
