package com.park.persistence.repository.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.park.exception.CacheException;
import com.park.persistence.entity.Condutor;
import com.park.persistence.entity.Estacionamento;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class EstacionamentoRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public EstacionamentoRedisRepository(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void save(Condutor condutor, Estacionamento estacionamento) {
        try {
            String condutorJson = objectMapper.writeValueAsString(condutor);
            redisTemplate.opsForValue().set(
                    condutor.getId(),
                    condutorJson,
                    estacionamento.getTempoPermanencia(),
                    TimeUnit.HOURS
            );
        } catch (JsonProcessingException e) {
            throw new CacheException("Erro ao converter formas de pagamento para salvar no cache");
        }
    }

    public void save(Condutor condutor, int ttl) {
        try {
            String condutorJson = objectMapper.writeValueAsString(condutor);
            redisTemplate.opsForValue().set(
                    condutor.getId(),
                    condutorJson,
                    ttl,
                    TimeUnit.HOURS
            );
        } catch (JsonProcessingException e) {
            throw new CacheException("Erro ao converter formas de pagamento para salvar no cache");
        }
    }

    public void save(Condutor condutor, Estacionamento estacionamento, int ttl) {
        try {
            String KEY = condutor.getId() + "::PLACA::" + estacionamento.getPlaca();
            String condutorJson = objectMapper.writeValueAsString(condutor);
            redisTemplate.opsForValue().set(
                    KEY,
                    condutorJson,
                    ttl,
                    TimeUnit.HOURS
            );
        } catch (JsonProcessingException e) {
            throw new CacheException("Erro ao converter formas de pagamento para salvar no cache");
        }
    }


    public Condutor get(Condutor condutor) {
        Object condutorCache =  redisTemplate.opsForValue().get(condutor.getId());

        if (condutorCache == null) {
            return null;
        }
        try {
            return objectMapper.readValue((String) condutorCache, Condutor.class);
        } catch (JsonProcessingException e) {
            throw new CacheException("Erro ao converter condutor do cache para objeto");
        }
    }

    public void delete(Condutor condutor) {
        redisTemplate.delete(condutor.getId());
    }
}