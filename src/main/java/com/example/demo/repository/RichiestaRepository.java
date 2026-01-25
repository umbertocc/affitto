package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Richiesta;

public interface RichiestaRepository extends JpaRepository<Richiesta, Long> {}


