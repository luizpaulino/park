package com.park.controller;

import com.park.persistence.entity.Condutor;
import com.park.persistence.entity.Estacionamento;
import com.park.persistence.entity.Veiculo;
import com.park.service.CondutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/condutores")
public class CondutorController {

    private final CondutorService condutorService;

    @Autowired
    public CondutorController(CondutorService condutorService) {
        this.condutorService = condutorService;
    }

    @Operation(summary = "Criar um novo condutor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Condutor criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Condutor> criarCondutor(@Valid @RequestBody Condutor condutor) {
        Condutor novoCondutor = condutorService.criarCondutor(condutor);
        return new ResponseEntity<>(novoCondutor, HttpStatus.CREATED);
    }

    @Operation(summary = "Obter todos os condutores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de condutores obtida com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"error\": \"Erro interno no servidor\",\n  \"details\": \"Entre em contato com o suporte\"\n}")))
    })
    @GetMapping
    public ResponseEntity<List<Condutor>> obterTodosCondutores() {
        List<Condutor> condutores = condutorService.obterTodosCondutores();
        return new ResponseEntity<>(condutores, HttpStatus.OK);
    }

    @Operation(summary = "Obter condutor por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Condutor encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Condutor não encontrado", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"error\": \"Condutor não encontrado\",\n  \"details\": \"Verifique o ID informado\"\n}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"error\": \"Erro interno no servidor\",\n  \"details\": \"Entre em contato com o suporte\"\n}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Condutor> obterCondutorPorId(
            @Parameter(description = "ID do condutor", required = true) @PathVariable String id
    ) throws ChangeSetPersister.NotFoundException {
        return new ResponseEntity<>(condutorService.obterCondutorPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Adicionar veículo ao condutor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veículo adicionado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Condutor não encontrado", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"error\": \"Condutor não encontrado\",\n  \"details\": \"Verifique o ID informado\"\n}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"error\": \"Erro interno no servidor\",\n  \"details\": \"Entre em contato com o suporte\"\n}")))
    })
    @PostMapping("/{id}/veiculos")
    public ResponseEntity<Condutor> adicionarVeiculoAoCondutor(
            @Parameter(description = "ID do condutor", required = true) @PathVariable String id,
            @Parameter(description = "Dados do veículo a ser adicionado", required = true) @Valid @RequestBody Veiculo veiculo
    ) throws ChangeSetPersister.NotFoundException {
        Condutor condutor = condutorService.adicionarVeiculoAoCondutor(id, veiculo);
        return new ResponseEntity<>(condutor, HttpStatus.OK);
    }

    @Operation(summary = "Alterar dados do condutor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Condutor alterado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Condutor não encontrado", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"error\": \"Condutor não encontrado\",\n  \"details\": \"Verifique o ID informado\"\n}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"error\": \"Erro interno no servidor\",\n  \"details\": \"Entre em contato com o suporte\"\n}")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Condutor> alterarCondutor(
            @Parameter(description = "ID do condutor", required = true) @PathVariable String id,
            @Parameter(description = "Novos dados do condutor", required = true) @Valid @RequestBody Condutor condutor
    ) throws ChangeSetPersister.NotFoundException {
        Condutor condutorAtualizado = condutorService.alterarCondutor(id, condutor);
        return new ResponseEntity<>(condutorAtualizado, HttpStatus.OK);
    }

    @Operation(summary = "Remover veículo do condutor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Veículo removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Condutor não encontrado", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"error\": \"Condutor não encontrado\",\n  \"details\": \"Verifique o ID informado\"\n}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"error\": \"Erro interno no servidor\",\n  \"details\": \"Entre em contato com o suporte\"\n}")))
    })
    @DeleteMapping("/{idCondutor}/veiculos/{placa}")
    public ResponseEntity<Void> removerVeiculoDoCondutor(
            @Parameter(description = "ID do condutor", required = true) @PathVariable String idCondutor,
            @Parameter(description = "Placa do veículo a ser removido", required = true) @PathVariable String placa
    ) throws ChangeSetPersister.NotFoundException {
        condutorService.removerVeiculoDoCondutor(idCondutor, placa);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Adicionar estacionamento ao condutor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estacionamento adicionado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Condutor não encontrado", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"error\": \"Condutor não encontrado\",\n  \"details\": \"Verifique o ID informado\"\n}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"error\": \"Erro interno no servidor\",\n  \"details\": \"Entre em contato com o suporte\"\n}")))
    })
    @PostMapping("/{id}/estacionamentos")
    public ResponseEntity<Condutor> adicionarEstacionamentoAoCondutor(
            @Parameter(description = "ID do condutor", required = true) @PathVariable String id,
            @Parameter(description = "Dados do estacionamento a ser adicionado", required = true) @Valid @RequestBody Estacionamento estacionamento
    ) throws ChangeSetPersister.NotFoundException {
        Condutor condutor = condutorService.adicionarEstacionamentoAoCondutor(id, estacionamento);
        return new ResponseEntity<>(condutor, HttpStatus.OK);
    }

    @Operation(summary = "Remover estacionamento do condutor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Estacionamento removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Condutor não encontrado", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"error\": \"Condutor não encontrado\",\n  \"details\": \"Verifique o ID informado\"\n}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"error\": \"Erro interno no servidor\",\n  \"details\": \"Entre em contato com o suporte\"\n}")))
    })
    @DeleteMapping("/{idCondutor}/estacionamentos/{placa}")
    public ResponseEntity<Void> removerEstacionamentoDoCondutor(
            @Parameter(description = "ID do condutor", required = true) @PathVariable String idCondutor,
            @Parameter(description = "Placa do estacionamento a ser removido", required = true) @PathVariable String placa
    ) throws ChangeSetPersister.NotFoundException {
        condutorService.removerEstacionamentoDoCondutor(idCondutor, placa);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
