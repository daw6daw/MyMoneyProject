package com.petprojects.mymoneyproject.controller;

import com.petprojects.mymoneyproject.DTO.GenericDTO;
import com.petprojects.mymoneyproject.model.GenericModel;
import com.petprojects.mymoneyproject.repository.GenericRepository;
import com.petprojects.mymoneyproject.service.GenericService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public abstract class GenericController<T extends GenericModel, N extends GenericDTO> {

    private final GenericService<T, N> service;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public GenericController(GenericService<T, N> service) {
        this.service = service;
    }

    @Operation(description = "Получить все записи", method = "getAll")
    @RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<N>> getAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getAll());
    }

    @Operation(description = "Получить запись по id", method = "getById")
    @RequestMapping(value = "/getById", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<N> getOneById(@RequestParam(value = "id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getOne(id));
    }

    @Operation(description = "Создание записи", method = "create")
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<N> create(@RequestBody N newEntity) {
        newEntity.setCreatedWhen(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(newEntity));
    }

    @Operation(description = "Обновление записи", method = "updateById")
    @RequestMapping(value = "/updateById", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<N> update(@RequestBody N updatedEntity, @RequestParam(value = "id") Long id) {
        updatedEntity.setId(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.update(updatedEntity));
    }

    @Operation(description = "Удаление записи", method = "deleteById")
    @RequestMapping(value = "/deleteById", method = RequestMethod.DELETE)
    public void delete(@RequestParam(value = "id") Long id) {
        service.delete(id);
    }

}
