package com.example.U5_W7_D5.services;

import com.example.U5_W7_D5.dto.PrenotazioneDTO;
import com.example.U5_W7_D5.entities.Evento;
import com.example.U5_W7_D5.entities.Prenotazione;
import com.example.U5_W7_D5.entities.User;
import com.example.U5_W7_D5.repositories.EventoRepository;
import com.example.U5_W7_D5.repositories.PrenotazioneRepository;
import com.example.U5_W7_D5.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PrenotazioneService {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Prenotazione> getPrenotazioniUtente() {
        User user = getCurrentUser();
        return prenotazioneRepository.findByUserId(user.getId());
    }

    public List<Prenotazione> getPrenotazioniEvento(Long eventoId) {
        return prenotazioneRepository.findByEventoId(eventoId);
    }

    @Transactional
    public Prenotazione createPrenotazione(PrenotazioneDTO dto) {
        User user = getCurrentUser();
        Evento evento = eventoRepository.findById(dto.getEventoId())
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));

        // Verifica se l'utente ha già una prenotazione per questo evento
        if (prenotazioneRepository.findByUserIdAndEventoId(user.getId(), evento.getId()).isPresent()) {
            throw new RuntimeException("Hai già una prenotazione per questo evento");
        }

        // Verifica se ci sono posti disponibili
        if (evento.getPostiDisponibili() < dto.getNumeroPosti()) {
            throw new RuntimeException("Non ci sono abbastanza posti disponibili");
        }

        // Crea la prenotazione
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setUser(user);
        prenotazione.setEvento(evento);
        prenotazione.setNumeroPosti(dto.getNumeroPosti());
        prenotazione.setDataPrenotazione(LocalDateTime.now());

        // Aggiorna i posti disponibili
        evento.setPostiDisponibili(evento.getPostiDisponibili() - dto.getNumeroPosti());
        eventoRepository.save(evento);

        return prenotazioneRepository.save(prenotazione);
    }

    @Transactional
    public void cancellaPrenotazione(Long id) {
        Prenotazione prenotazione = prenotazioneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prenotazione non trovata"));

        User user = getCurrentUser();

        if (!prenotazione.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Non sei autorizzato a cancellare questa prenotazione");
        }

        // Ripristina i posti disponibili
        Evento evento = prenotazione.getEvento();
        evento.setPostiDisponibili(evento.getPostiDisponibili() + prenotazione.getNumeroPosti());
        eventoRepository.save(evento);

        prenotazioneRepository.delete(prenotazione);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
    }
}

