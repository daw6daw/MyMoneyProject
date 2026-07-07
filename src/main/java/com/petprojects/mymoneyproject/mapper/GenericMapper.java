package com.petprojects.mymoneyproject.mapper;

import com.petprojects.mymoneyproject.DTO.GenericDTO;
import com.petprojects.mymoneyproject.model.GenericModel;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Objects;

public abstract class GenericMapper<E extends GenericModel, D extends GenericDTO> implements Mapper<E, D> {

    protected final ModelMapper modelMapper;

    private final Class<E> entityClass;

    private final Class<D> dtoClass;

    protected GenericMapper (ModelMapper modelMapper, Class<E> entityClass, Class<D> dtoClass) {
        this.modelMapper = modelMapper;
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
    }

    @Override
    public E toEntity (D dto) {
        return Objects.isNull(dto)
                ? null
                : modelMapper.map(dto, entityClass);
    }

    @Override
    public D toDTO (E entity) {
        return Objects.isNull(entity)
                ? null
                : modelMapper.map(entity, dtoClass);
    }

    @Override
    public List<E> toEntities (List<D> dtoList) {
        return dtoList.stream().map(this::toEntity).toList();
    }

    @Override
    public List<D> toDTOs (List<E> entitiesList) {
        return entitiesList.stream().map(this::toDTO).toList();
    }
}
