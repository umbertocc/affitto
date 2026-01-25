package com.example.demo.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor
@Entity
@Table(name = "richiesta")
public class Richiesta {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long propertyId;
  private String checkin, checkout;
  private String nome, email, messaggio;
  private LocalDateTime createdAt = LocalDateTime.now();
  // getters/setters
}