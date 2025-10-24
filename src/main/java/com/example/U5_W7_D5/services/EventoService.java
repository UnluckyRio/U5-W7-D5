package com.example.U5_W7_D5.services;

import com.example.U5_W7_D5.dto.EventoDTO;
import com.example.U5_W7_D5.entities.Evento;
import com.example.U5_W7_D5.entities.User;
import com.example.U5_W7_D5.enums.Role;
import com.example.U5_W7_D5.repositories.EventoRepository;
import com.example.U5_W7_D5.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Evento> getAllEventi() {
        return eventoRepository.findAll();
    }

    public List<Evento> getEventiDisponibili() {
        return eventoRepository.findByPostiDisponibiliGreaterThan(0);
    }

    public List<Evento> getEventiFuturi() {
        return eventoRepository.findByDataAfter(LocalDate.now());
    }

    public Evento getEventoById(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento non trovato con id: " + id));
    }

    public List<Evento> getEventiByOrganizzatore() {
        User organizzatore = getCurrentUser();
        return eventoRepository.findByOrganizzatoreId(organizzatore.getId());
    }

    @Transactional
    public Evento createEvento(EventoDTO dto) {
        User organizzatore = getCurrentUser();

        if (organizzatore.getRole() != Role.ORGANIZER) {
            throw new RuntimeException("Solo gli organizzatori possono creare eventi");
        }

        Evento evento = new Evento();
        evento.setTitolo(dto.getTitolo());
        evento.setDescrizione(dto.getDescrizione());
        evento.setData(dto.getData());
        evento.setOra(dto.getOra());
        evento.setLuogo(dto.getLuogo());
        evento.setPostiTotali(dto.getPostiTotali());
        evento.setPostiDisponibili(dto.getPostiTotali());
        evento.setOrganizzatore(organizzatore);

        return eventoRepository.save(evento);
    }

    @Transactional
    public Evento updateEvento(Long id, EventoDTO dto) {
        Evento evento = getEventoById(id);
        User organizzatore = getCurrentUser();

        if (!evento.getOrganizzatore().getId().equals(organizzatore.getId())) {
            throw new RuntimeException("Non sei autorizzato a modificare questo evento");
        }

        int postiPrenotati = evento.getPostiTotali() - evento.getPostiDisponibili();

        if (dto.getPostiTotali() < postiPrenotati) {
            throw new RuntimeException("Non puoi ridurre i posti totali sotto il numero di posti già prenotati");
        }

        evento.setTitolo(dto.getTitolo());
        evento.setDescrizione(dto.getDescrizione());
        evento.setData(dto.getData());
        evento.setOra(dto.getOra());
        evento.setLuogo(dto.getLuogo());
        evento.setPostiTotali(dto.getPostiTotali());
        evento.setPostiDisponibili(evento.getPostiDisponibili() + (dto.getPostiTotali() - evento.getPostiTotali()));

        return eventoRepository.save(evento);
    }

    @Transactional
    public void deleteEvento(Long id) {
        Evento evento = getEventoById(id);
        User organizzatore = getCurrentUser();

        if (!evento.getOrganizzatore().getId().equals(organizzatore.getId())) {
            throw new RuntimeException("Non sei autorizzato a eliminare questo evento");
        }

        eventoRepository.delete(evento);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
    }
}

