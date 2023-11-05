package com.park.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.park.persistence.entity.Condutor;

import java.util.Optional;

public interface CondutorRepository extends MongoRepository<Condutor, String> {
    Optional<Condutor> findByCpf(String cpf);
}
