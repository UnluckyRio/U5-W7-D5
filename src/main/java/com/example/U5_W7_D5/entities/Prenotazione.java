package com.example.U5_W7_D5.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "prenotazioni")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prenotazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @Column(nullable = false)
    private Integer numeroPosti;

    @Column(nullable = false)
    private LocalDateTime dataPrenotazione;

    @PrePersist
    protected void onCreate() {
        dataPrenotazione = LocalDateTime.now();
    }
}

