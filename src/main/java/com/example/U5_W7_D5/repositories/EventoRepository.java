package com.example.U5_W7_D5.repositories;

import com.example.U5_W7_D5.entities.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByOrganizzatoreId(Long organizzatoreId);

    List<Evento> findByDataAfter(LocalDate data);

    List<Evento> findByPostiDisponibiliGreaterThan(Integer posti);
}

