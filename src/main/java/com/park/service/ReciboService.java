package com.park.service;

import com.park.persistence.entity.Condutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Service
public class ReciboService {

    private final ValorService valorService;

    @Autowired
    public ReciboService(ValorService valorService) {
        this.valorService = valorService;
    }

    public String reciboExpirado(Condutor condutor) {
        LocalDateTime dataAtual = LocalDateTime.now();

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        String dataAtualFormatada = formato.format(dataAtual);

        long horasTotais = ChronoUnit.HOURS.between(condutor.getEstacionamentos().get(0).getDataEntrada(), dataAtual);

        if (horasTotais == 0) {
            horasTotais = 1;
        }

        int totalHorasSolicitadas = condutor.getEstacionamentos().get(0).getTempoPermanencia();

        Double valorTotal = totalHorasSolicitadas * 10.00;

        return "Recibo de pagamento do estacionamento: \n" +
                "Valor: R$ " + valorTotal + "\n" +
                "Data: " + condutor.getEstacionamentos().get(0).getDataEntrada()+ " \n" +
                "Hora de saída: " + dataAtualFormatada + " \n" +
                "Tempo de permanência: " + horasTotais + " horas \n" +
                "Tempo de permanência solicitado: " + totalHorasSolicitadas + " horas \n" +
                "Veículo: " + condutor.getEstacionamentos().get(0).getPlaca();
    }

    public String recibo(Condutor condutor) {
        LocalDateTime dataAtual = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dataAtualFormatada = formato.format(dataAtual);

        Duration duracao = Duration.between(condutor.getEstacionamentos().get(0).getDataEntrada(), dataAtual);
        long horasTotais = duracao.toMinutes() <= 1 ? 0 : duracao.toHours() + 1;

        BigDecimal valor = valorService.calcularValor(condutor.getEstacionamentos().get(0).getDataEntrada(), dataAtual);
        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        String valorFormatado = formatoMoeda.format(valor);

        return "Recibo de pagamento do estacionamento: \n" +
                "Valor: " + valorFormatado + "\n" +
                "Data: " + condutor.getEstacionamentos().get(0).getDataEntrada()+ " \n" +
                "Hora de saída: " + dataAtualFormatada + " \n" +
                "Tempo de permanência: " + horasTotais + " horas \n" +
                "Veículo: " + condutor.getEstacionamentos().get(0).getPlaca();
    }
}
