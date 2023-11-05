package com.park.service;

import com.park.exception.CondutorExistenteException;
import com.park.persistence.entity.*;
import com.park.persistence.repository.CondutorRepository;
import com.park.persistence.repository.cache.EstacionamentoRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@Service
public class CondutorService {

    private final CondutorRepository condutorRepository;

    private final EstacionamentoRedisRepository estacionamentoRedisRepository;
    private final EmailService emailService;
    private final ReciboService reciboService;

    @Autowired
    public CondutorService(
            CondutorRepository condutorRepository,
            EstacionamentoRedisRepository estacionamentoRedisRepository,
            EmailService emailService,
            ReciboService reciboService
    ) {

        this.condutorRepository = condutorRepository;
        this.estacionamentoRedisRepository = estacionamentoRedisRepository;
        this.emailService = emailService;
        this.reciboService = reciboService;
    }

    public Condutor criarCondutor(Condutor condutor) {
        Optional<Condutor> condutorExistente = condutorRepository.findByCpf(condutor.getCpf());

        if (condutorExistente.isPresent()) {
            throw new CondutorExistenteException("Condutor com CPF " + condutor.getCpf() + " já existe.");
        }

        if (condutor.getVeiculos() == null || condutor.getVeiculos().isEmpty()) {
            throw new IllegalArgumentException("Condutor deve ter pelo menos um veículo.");
        }

        if (condutor.getEstacionamentos() != null && !condutor.getEstacionamentos().isEmpty()) {
            throw new IllegalArgumentException("Condutor não deve ter estacionamentos ao ser criado.");
        }

        if (condutor.getFormaPagamento() == null || !EnumSet.of(FormaPagamento.PIX, FormaPagamento.CARTAO_CREDITO, FormaPagamento.CARTAO_DEBITO).contains(condutor.getFormaPagamento())) {
            throw new IllegalArgumentException("Forma de pagamento inválida.");
        }

        return condutorRepository.save(condutor);
    }

    public List<Condutor> obterTodosCondutores() {
        return condutorRepository.findAll();
    }

    public Condutor obterCondutorPorId(String id) throws ChangeSetPersister.NotFoundException {
        return condutorRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

    public Condutor adicionarVeiculoAoCondutor(String id, Veiculo veiculo) throws ChangeSetPersister.NotFoundException {
        Condutor condutor = condutorRepository.findById(id)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        condutor.getVeiculos().add(veiculo);

        return condutorRepository.save(condutor);
    }

    public Condutor alterarCondutor(String id, Condutor condutor) throws ChangeSetPersister.NotFoundException {
        Condutor condutorExistente = condutorRepository.findById(id)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        if (!condutor.getCpf().equals(condutorExistente.getCpf())) {
            throw new IllegalArgumentException("Não é possível alterar o CPF do condutor.");
        }

        return condutorRepository.save(condutor);
    }

    public void removerVeiculoDoCondutor(String idCondutor, String placa) throws ChangeSetPersister.NotFoundException {
        Condutor condutor = condutorRepository.findById(idCondutor)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        condutor.getVeiculos().removeIf(veiculo -> veiculo.getPlaca().equals(placa));

        condutorRepository.save(condutor);
    }

    public Condutor adicionarEstacionamentoAoCondutor(
            String id,
            Estacionamento estacionamento
    ) throws ChangeSetPersister.NotFoundException {
        Condutor condutor = condutorRepository.findById(id)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        if (FormaPagamento.PIX.equals(condutor.getFormaPagamento())
                && !TipoEstacionamento.FIXO.equals(estacionamento.getTipoEstacionamento())
        ) {
            throw new IllegalArgumentException("Se a forma de pagamento for PIX, o tipo do estacionamento deve ser FIXO.");
        }

        if (!condutor.getEstacionamentos().isEmpty()) {
            throw new IllegalArgumentException("O condutor já possui um estacionamento registrado.");
        }

        condutor.getEstacionamentos().add(estacionamento);
        condutorRepository.save(condutor);

        if (TipoEstacionamento.FIXO.equals(estacionamento.getTipoEstacionamento())) {
            estacionamentoRedisRepository.save(condutor, estacionamento);
            int ttlPrestesExpirar = (estacionamento.getTempoPermanencia() * 60) - 10;
            estacionamentoRedisRepository.save(condutor, estacionamento, ttlPrestesExpirar);
        }

        if (TipoEstacionamento.ROTATIVO.equals(estacionamento.getTipoEstacionamento())) {
            int ttlRotativo = 60;
            estacionamentoRedisRepository.save(condutor, ttlRotativo);
            estacionamentoRedisRepository.save(condutor, estacionamento, ttlRotativo - 10);
        }

        return condutor;
    }

    public void removerEstacionamentoDoCondutor(String idCondutor, String placa) throws ChangeSetPersister.NotFoundException {
        Condutor condutor = condutorRepository.findById(idCondutor)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        Optional<Estacionamento> estacionamentoOpt = condutor.getEstacionamentos()
                .stream()
                .filter(e -> e.getPlaca().equals(placa))
                .findFirst();

        if (estacionamentoOpt.isEmpty()) {
            throw new IllegalArgumentException("Estacionamento com placa " + placa + " não encontrado.");
        }

        if (TipoEstacionamento.ROTATIVO.equals(estacionamentoOpt.get().getTipoEstacionamento())) {
            String recibo = reciboService.recibo(condutor);

            emailService.enviarEmail(condutor.getEmail(), "Recibo do estacionamento finalizado", recibo);
        }

        condutor.getEstacionamentos().remove(estacionamentoOpt.get());

        condutorRepository.save(condutor);

        estacionamentoRedisRepository.delete(condutor);
    }


    public Condutor consultarRedis(String chave) throws ChangeSetPersister.NotFoundException {

        Condutor condutor = condutorRepository.findById(chave).orElseThrow(ChangeSetPersister.NotFoundException::new);

        return estacionamentoRedisRepository.get(condutor);
    }

    public void renovaEstacionamentoRotativo(Condutor condutor, Estacionamento estacionamento) {

        int ttlRotativo = 10;
        estacionamentoRedisRepository.save(condutor, ttlRotativo);
        estacionamentoRedisRepository.save(condutor, estacionamento, ttlRotativo - 1);

    }
}
