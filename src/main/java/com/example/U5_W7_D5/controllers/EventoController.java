package com.example.U5_W7_D5.controllers;

import com.example.U5_W7_D5.dto.EventoDTO;
import com.example.U5_W7_D5.entities.Evento;
import com.example.U5_W7_D5.services.EventoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventi")
@CrossOrigin(origins = "*")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @GetMapping("/lista")
    public ResponseEntity<List<Evento>> getAllEventi() {
        return ResponseEntity.ok(eventoService.getAllEventi());
    }

    @GetMapping("/disponibili")
    public ResponseEntity<List<Evento>> getEventiDisponibili() {
        return ResponseEntity.ok(eventoService.getEventiDisponibili());
    }

    @GetMapping("/futuri")
    public ResponseEntity<List<Evento>> getEventiFuturi() {
        return ResponseEntity.ok(eventoService.getEventiFuturi());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evento> getEventoById(@PathVariable Long id) {
        return ResponseEntity.ok(eventoService.getEventoById(id));
    }

    @GetMapping("/miei-eventi")
    @PreAuthorize("hasAuthority('ORGANIZER')")
    public ResponseEntity<List<Evento>> getMieiEventi() {
        return ResponseEntity.ok(eventoService.getEventiByOrganizzatore());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ORGANIZER')")
    public ResponseEntity<Evento> createEvento(@Valid @RequestBody EventoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoService.createEvento(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ORGANIZER')")
    public ResponseEntity<Evento> updateEvento(@PathVariable Long id, @Valid @RequestBody EventoDTO dto) {
        return ResponseEntity.ok(eventoService.updateEvento(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ORGANIZER')")
    public ResponseEntity<Void> deleteEvento(@PathVariable Long id) {
        eventoService.deleteEvento(id);
        return ResponseEntity.noContent().build();
    }
}

