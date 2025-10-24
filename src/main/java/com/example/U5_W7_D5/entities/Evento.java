package com.example.U5_W7_D5.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "eventi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titolo;

    @Column(nullable = false, length = 1000)
    private String descrizione;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private LocalTime ora;

    @Column(nullable = false)
    private String luogo;

    @Column(nullable = false)
    private Integer postiTotali;

    @Column(nullable = false)
    private Integer postiDisponibili;

    @ManyToOne
    @JoinColumn(name = "organizzatore_id", nullable = false)
    private User organizzatore;
    @JsonIgnore

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL)
    private List<Prenotazione> prenotazioni;
}

