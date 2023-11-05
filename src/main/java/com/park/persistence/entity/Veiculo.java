package com.park.persistence.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Veiculo {

    @NotBlank(message = "A placa do veículo é obrigatória")
    private String placa;

    @NotBlank(message = "A marca do veículo é obrigatória")
    private String marca;

    @NotBlank(message = "O modelo do veículo é obrigatório")
    private String modelo;
}
