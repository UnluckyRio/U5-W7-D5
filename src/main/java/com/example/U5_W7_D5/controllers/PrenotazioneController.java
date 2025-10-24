package com.example.U5_W7_D5.controllers;

import com.example.U5_W7_D5.dto.PrenotazioneDTO;
import com.example.U5_W7_D5.entities.Prenotazione;
import com.example.U5_W7_D5.services.PrenotazioneService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prenotazioni")
@CrossOrigin(origins = "*")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;

    @GetMapping("/mie-prenotazioni")
    @PreAuthorize("hasAnyAuthority('USER', 'ORGANIZER')")
    public ResponseEntity<List<Prenotazione>> getMiePrenotazioni() {
        return ResponseEntity.ok(prenotazioneService.getPrenotazioniUtente());
    }

    @GetMapping("/evento/{eventoId}")
    @PreAuthorize("hasAuthority('ORGANIZER')")
    public ResponseEntity<List<Prenotazione>> getPrenotazioniEvento(@PathVariable Long eventoId) {
        return ResponseEntity.ok(prenotazioneService.getPrenotazioniEvento(eventoId));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ORGANIZER')")
    public ResponseEntity<Prenotazione> createPrenotazione(@Valid @RequestBody PrenotazioneDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prenotazioneService.createPrenotazione(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ORGANIZER')")
    public ResponseEntity<Void> cancellaPrenotazione(@PathVariable Long id) {
        prenotazioneService.cancellaPrenotazione(id);
        return ResponseEntity.noContent().build();
    }
}


