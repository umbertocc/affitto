package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Booking;
import com.example.demo.entity.Richiesta;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.RichiestaRepository;
import com.example.demo.service.BookingService;
import com.example.demo.service.EmailService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private RichiestaRepository richiestaRepository;

    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private EmailService emailService;

    @GetMapping("/disponibilita")
    public List<Map<String, Object>> getDisponibilita(
            @RequestParam Long propertyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        
        List<Booking> prenotazioni = bookingRepository.findOverlappingBookings(propertyId, start, end);
        
        return prenotazioni.stream().map(b -> Map.<String, Object>of(  // Cast esplicito 
                "title", "Occupato (" + b.getGuestCount() + " ospiti)",
                "start", b.getStartDate().toString(),
                "end", b.getEndDate().toString(),
                "backgroundColor", "#dc3545",
                "borderColor", "#dc3545"
        )).collect(Collectors.toList());
    }

    // POST /api/bookings - Crea booking
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        if (bookingService.hasOverlap(booking)) {
            return ResponseEntity.badRequest().body(null);
        }
        Booking saved = bookingRepository.save(booking);
        return ResponseEntity.ok(saved);
    }

    // GET /api/bookings?propertyId=1 - Elenco bookings per property
    @GetMapping
    public List<Booking> getBookingsByProperty(@RequestParam Long propertyId) {
        return bookingRepository.findByPropertyId(propertyId);
    }

    // GET /api/bookings/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable Long id) {
        return bookingRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/bookings/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/available")
    public ResponseEntity<Map<String, Boolean>> checkAvailable(
            @RequestParam Long propertyId,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        boolean available = bookingService.isAvailable(propertyId, start, end);
        return ResponseEntity.ok(Map.of("available", available));
    }
    
    @GetMapping("/disponibilita-all")
    public List<Map<String, Object>> getAllDisponibilita(@RequestParam Long propertyId) {
        List<Booking> tuttePrenotazioni = bookingRepository.findByPropertyId(propertyId);  // TUTTI bookings
        return tuttePrenotazioni.stream().map(b -> Map.<String, Object>of(  // Cast esplicito 
                "title", "Occupato (" + b.getGuestCount() + " ospiti)",
                "start", b.getStartDate().toString(),
                "end", b.getEndDate().plusDays(1).toString(),
                "backgroundColor", "#dc3545",
                "borderColor", "#dc3545"
        )).collect(Collectors.toList());
    }
    
    @PostMapping("/richieste")
    public ResponseEntity<String> inviaRichiesta(@RequestBody Richiesta richiesta) {
      richiestaRepository.save(richiesta);
      
      // INVIA EMAIL (Spring Mail)
      emailService.inviaEmailProprietario(richiesta);
      
      return ResponseEntity.ok("Richiesta inviata!");
    }
}
