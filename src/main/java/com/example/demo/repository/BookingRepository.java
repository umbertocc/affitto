package com.example.demo.repository;

import com.example.demo.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Elenco bookings per property (es. TorrePali id=1)
    List<Booking> findByPropertyId(Long propertyId);

    // Query overlap per /disponibilita: bookings che intersecano il range
    // startDate <= end AND endDate >= start
    @Query("SELECT b FROM Booking b WHERE b.property.id = :propertyId " +
           "AND b.startDate <= :endDate " +
           "AND b.endDate >= :startDate")
    List<Booking> findOverlappingBookings(@Param("propertyId") Long propertyId,
                                          @Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate);


}
