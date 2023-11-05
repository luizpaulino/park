package com.park.service;

import com.park.persistence.entity.Condutor;
import com.park.persistence.entity.Estacionamento;
import com.park.persistence.entity.TipoEstacionamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class NotificacaoService {

    private final CondutorService condutorService;
    private final EmailService emailService;
    private final ReciboService reciboService;

    private final Logger logger = Logger.getLogger(NotificacaoService.class.getName());

    @Autowired
    public NotificacaoService(
            CondutorService condutorService,
            EmailService emailService,
            ReciboService reciboService
    ) {
        this.condutorService = condutorService;
        this.emailService = emailService;
        this.reciboService = reciboService;
    }

    public void notificar(String chave) throws ChangeSetPersister.NotFoundException {

        if (chave.contains("::PLACA::")) {
            processarChaveComEstacionamento(chave);
        } else {
            notificarEstacionamentoExpirado(chave);
        }
    }

    private void processarChaveComEstacionamento(String chave) throws ChangeSetPersister.NotFoundException {
        chave = chave.split("::")[0];

        Condutor condutor = condutorService.consultarRedis(chave);

        if (condutor != null) {
            Estacionamento estacionamento = condutor.getEstacionamentos().get(0);

            if (TipoEstacionamento.FIXO.equals(estacionamento.getTipoEstacionamento())) {
                notificarEstacionamentoPrestesAExpirar(condutor);
            } else {
                notificarExtensaoAutomatica(condutor);
            }
        }
    }

    private void notificarEstacionamentoPrestesAExpirar(Condutor condutor) {
        Estacionamento estacionamento = condutor.getEstacionamentos().get(0);
        if (TipoEstacionamento.ROTATIVO.equals(estacionamento.getTipoEstacionamento())) {
            condutorService.renovaEstacionamentoRotativo(condutor, estacionamento);
        } else {
            String destinatario = condutor.getEmail();
            String assunto = "Alerta de Estacionamento";
            String mensagem = "Seu tempo de estacionamento está prestes a expirar, faltam 10 minutos. Por favor, mova o veículo.";

            emailService.enviarEmail(destinatario, assunto, mensagem);
        }
    }

    private void notificarEstacionamentoExpirado(String chave) throws ChangeSetPersister.NotFoundException {

        Condutor condutor = condutorService.obterCondutorPorId(chave);

        Estacionamento estacionamento = condutor.getEstacionamentos().get(0);
        if (TipoEstacionamento.ROTATIVO.equals(estacionamento.getTipoEstacionamento())) {
            condutorService.renovaEstacionamentoRotativo(condutor, estacionamento);
        } else {
            String assunto = "Estacionamento expirado";

            String mensagem = reciboService.reciboExpirado(condutor);
            emailService.enviarEmail(condutor.getEmail(), assunto, mensagem);

            condutorService.removerEstacionamentoDoCondutor(condutor.getId(), condutor.getEstacionamentos().get(0).getPlaca());
        }
    }

    private void notificarExtensaoAutomatica(Condutor condutor) {
        String destinatario = condutor.getEmail();
        String assunto = "Alerta de Estacionamento";
        String mensagem = "Seu tempo de estacionamento está prestes a expirar, faltam 10 minutos. Vamos renovar por mais uma hora.";

        emailService.enviarEmail(destinatario, assunto, mensagem);
    }
}
