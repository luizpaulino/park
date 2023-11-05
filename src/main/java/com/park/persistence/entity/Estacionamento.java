package com.park.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
public class Estacionamento {

    @NotBlank(message = "O tipo de estacionamento é obrigatório")
    private TipoEstacionamento tipoEstacionamento;

    @NotBlank(message = "A placa do veículo é obrigatória")
    private String placa;

    @Positive(message = "O tempo de permanência deve ser positivo")
    private int tempoPermanencia;

    @JsonIgnore
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataEntrada = LocalDateTime.now();

    @JsonIgnore
    private Boolean finalizado = false;
}