package com.park.service;

import com.park.persistence.entity.Pagamento;
import com.park.persistence.repository.FormaPagamentoRepository;
import com.park.persistence.repository.cache.EstacionamentoRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FormaPagamentoService {

    private final FormaPagamentoRepository formaPagamentoRepository;
    @Autowired
    public FormaPagamentoService(
            FormaPagamentoRepository formaPagamentoRepository,
            EstacionamentoRedisRepository estacionamentoRedisRepository
    ) {
        this.formaPagamentoRepository = formaPagamentoRepository;
    }

    public Pagamento criarFormaPagamento(Pagamento formaPagamento) {
        return formaPagamentoRepository.save(formaPagamento);
    }

    public Optional<Pagamento> obterFormaPagamentoPorId(String id) {
        return formaPagamentoRepository.findById(id);
    }
}