package com.park.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class ValorService {

    private final int valorHora = 10;

    public BigDecimal calcularValor(LocalDateTime dataEntrada, LocalDateTime dataSaida) {
        Duration duracao = Duration.between(dataEntrada, dataSaida);

        long horasTotais = duracao.toMinutes() <= 1 ? 0 : duracao.toHours() + 1;
        return BigDecimal.valueOf(horasTotais * valorHora);
    }
}
