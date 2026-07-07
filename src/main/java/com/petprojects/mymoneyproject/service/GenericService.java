package com.petprojects.mymoneyproject.service;

import com.petprojects.mymoneyproject.DTO.GenericDTO;
import com.petprojects.mymoneyproject.mapper.GenericMapper;
import com.petprojects.mymoneyproject.model.GenericModel;
import com.petprojects.mymoneyproject.repository.GenericRepository;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

public abstract class GenericService <T extends GenericModel, N extends GenericDTO> {

    private final GenericRepository<T> repository;

    private final GenericMapper<T, N> mapper;

    public GenericService(GenericRepository<T> repository, GenericMapper<T, N> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<N> getAll () {
        return mapper.toDTOs(repository.findAll());
    }

    public N getOne (Long id) {
        return mapper.toDTO(repository.findById(id).orElseThrow(() -> new NotFoundException("Not found by this id: " + id)));
    }

    public N create (N newObject) {
        return mapper.toDTO(repository.save(mapper.toEntity(newObject)));
    }

    public N update (N updatedObject) {
        return mapper.toDTO(repository.save(mapper.toEntity(updatedObject)));
    }

    public void delete (Long id) {
        repository.deleteById(id);
    }
}
