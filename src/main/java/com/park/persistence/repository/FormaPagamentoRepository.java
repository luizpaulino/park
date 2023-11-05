package com.park.persistence.repository;

import com.park.persistence.entity.Pagamento;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FormaPagamentoRepository extends MongoRepository<Pagamento, String> {
}
