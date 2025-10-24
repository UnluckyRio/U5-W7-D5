package com.example.U5_W7_D5.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrenotazioneDTO {

    @NotNull(message = "L'ID dell'evento è obbligatorio")
    private Long eventoId;

    @NotNull(message = "Il numero di posti è obbligatorio")
    @Min(value = 1, message = "Devi prenotare almeno 1 posto")
    private Integer numeroPosti;
}

