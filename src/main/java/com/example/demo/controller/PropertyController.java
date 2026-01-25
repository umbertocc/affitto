package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Property;
import com.example.demo.repository.PropertyRepository;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {
    @Autowired private PropertyRepository repo;
    
    @GetMapping
    public List<Property> getAll() {
        return repo.findAll();
    }
    
    @PostMapping
    public Property create(@RequestBody Property p) {
        return repo.save(p);
    }
}
