package com.park.persistence.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Data
@Document
public class Pagamento {
    @Id
    private String id;

    @NotBlank(message = "O formaPagamento é obrigatório")
    private String formaPagamento;
}
