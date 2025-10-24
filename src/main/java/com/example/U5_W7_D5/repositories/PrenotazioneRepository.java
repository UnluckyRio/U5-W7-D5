package com.example.U5_W7_D5.repositories;

import com.example.U5_W7_D5.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
    List<Prenotazione> findByUserId(Long userId);

    List<Prenotazione> findByEventoId(Long eventoId);

    Optional<Prenotazione> findByUserIdAndEventoId(Long userId, Long eventoId);
}

