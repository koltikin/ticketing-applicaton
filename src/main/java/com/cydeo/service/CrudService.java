package com.cydeo.service;

import com.cydeo.dto.RoleDTO;

import java.util.List;

public interface CrudService<D,ID> {
    List<D> findAll();
    D findById(ID id);
    void save(D dto);
    void update(D dto);
    void delete(ID id);
}
