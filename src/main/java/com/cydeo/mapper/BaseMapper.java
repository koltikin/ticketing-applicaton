package com.cydeo.mapper;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

@AllArgsConstructor
public abstract class BaseMapper<D, E> {
    private final ModelMapper modelMapper;

    public D convertToDto(E entity){
        return modelMapper.map(entity,getDtoClass());
    }
    public E convertToEntity(D dto){
        return modelMapper.map(dto,getEntityClass());
    }
    public abstract Class<D> getDtoClass();
    public abstract Class<E> getEntityClass();

}
