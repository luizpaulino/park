package com.park.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Document
public class Condutor {

    @Id
    private String id;

    @NotBlank(message = "O nome é obrigatório")
    @NotNull(message = "O nome não pode ser nulo")
    private String nome;

    @NotBlank(message = "O endereço é obrigatório")
    @NotNull(message = "O endereço não pode ser nulo")
    private String endereco;

    @NotBlank(message = "O bairro é obrigatório")
    @NotNull(message = "O bairro não pode ser nulo")
    private String bairro;

    @NotBlank(message = "A cidade é obrigatória")
    @NotNull(message = "A cidade não pode ser nula")
    private String cidade;

    @NotBlank(message = "O estado é obrigatório e deve ter 2 caracteres")
    @Size(min = 2, max = 2, message = "O estado deve ter exatamente 2 caracteres")
    @NotNull(message = "O estado não pode ser nulo")
    private String estado;

    @NotBlank(message = "O CEP é obrigatório e deve ter 8 caracteres")
    @Size(min = 8, max = 8, message = "O CEP deve ter exatamente 8 caracteres")
    @NotNull(message = "O CEP não pode ser nulo")
    private String cep;

    @NotBlank(message = "O telefone é obrigatório e deve ter 11 caracteres")
    @Size(min = 11, max = 11, message = "O telefone deve ter exatamente 11 caracteres")
    @NotNull(message = "O telefone não pode ser nulo")
    private String telefone;

    @Email(message = "O e-mail deve ser válido")
    @NotBlank(message = "O e-mail é obrigatório")
    @NotNull(message = "O e-mail não pode ser nulo")
    private String email;

    @NotBlank(message = "O CPF é obrigatório e deve ter 11 caracteres")
    @Size(min = 11, max = 11, message = "O CPF deve ter exatamente 11 caracteres")
    @NotNull(message = "O CPF não pode ser nulo")
    private String cpf;

    @NotNull(message = "A forma de pagamento é obrigatória")
    @NotBlank(message = "A forma de pagamento é obrigatória")
    private FormaPagamento formaPagamento;

    private List<Veiculo> veiculos;

    private List<Estacionamento> estacionamentos = new ArrayList<>();
}
