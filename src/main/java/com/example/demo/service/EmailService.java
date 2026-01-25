package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Richiesta;

@Service
public class EmailService {
  @Autowired JavaMailSender mailSender;
  
  public void inviaEmailProprietario(Richiesta r) {
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setTo("tuoemail@torrepali.it");
    msg.setSubject("Nuova richiesta Torre Pali: " + r.getCheckin() + "→" + r.getCheckout());
    msg.setText("Da: " + r.getNome() + "\nEmail: " + r.getEmail() + "\n\n" + r.getMessaggio());
    mailSender.send(msg);
  }
}

