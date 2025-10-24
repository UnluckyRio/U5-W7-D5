package com.example.U5_W7_D5.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class EventoDTO {

    @NotBlank(message = "Il titolo è obbligatorio")
    private String titolo;

    @NotBlank(message = "La descrizione è obbligatoria")
    private String descrizione;

    @NotNull(message = "La data è obbligatoria")
    @Future(message = "La data deve essere futura")
    private LocalDate data;

    @NotNull(message = "L'ora è obbligatoria")
    private LocalTime ora;

    @NotBlank(message = "Il luogo è obbligatorio")
    private String luogo;

    @NotNull(message = "Il numero di posti totali è obbligatorio")
    @Min(value = 1, message = "Il numero di posti deve essere almeno 1")
    private Integer postiTotali;
}

