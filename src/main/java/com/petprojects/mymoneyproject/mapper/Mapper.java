package com.petprojects.mymoneyproject.mapper;

import com.petprojects.mymoneyproject.DTO.GenericDTO;
import com.petprojects.mymoneyproject.model.GenericModel;

import java.util.List;

public interface Mapper <E extends GenericModel, D extends GenericDTO> {

    E toEntity(D dto);

    D toDTO(E entity);

    List<E> toEntities(List<D> dtoList);

    List<D> toDTOs(List<E> entitiesList);
}
