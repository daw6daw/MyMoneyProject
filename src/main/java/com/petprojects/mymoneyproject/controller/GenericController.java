package com.petprojects.mymoneyproject.controller;

import com.petprojects.mymoneyproject.model.GenericModel;
import com.petprojects.mymoneyproject.repository.GenericRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;

@RestController
public abstract class GenericController<T extends GenericModel> {

    private final GenericRepository<T> genericRepository;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public GenericController(GenericRepository<T> genericRepository) {
        this.genericRepository = genericRepository;
    }

    @Operation(description = "Получить запись по id", method = "getById")
    @RequestMapping(value = "/getById", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<T> getOneById(@RequestParam(value = "id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(genericRepository.findById(id).orElseThrow(() -> new NotFoundException("Данных по переданному id не найдено, id: " + id)));
    }

    @Operation(description = "Создание записи", method = "create")
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<T> create(@RequestBody T newEntity) {
        newEntity.setCreatedWhen(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(genericRepository.save(newEntity));
    }

    @Operation(description = "Обновление записи", method = "updateById")
    @RequestMapping(value = "/updateById", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<T> update(@RequestBody T updatedEntity, @RequestParam(value = "id") Long id) {
        updatedEntity.setId(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(genericRepository.save(updatedEntity));
    }

    @Operation(description = "Удаление записи", method = "deleteById")
    @RequestMapping(value = "/deleteById", method = RequestMethod.DELETE)
    public void delete(@RequestParam(value = "id") Long id) {
        genericRepository.deleteById(id);
    }

}
