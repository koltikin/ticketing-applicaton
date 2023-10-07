package com.cydeo.mapper;

import com.cydeo.dto.RoleDTO;
import com.cydeo.entity.Role;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
public class RoleMapper extends BaseMapper<RoleDTO,Role>{


    public RoleMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    public Class<RoleDTO> getDtoClass() {
        return RoleDTO.class;
    }

    @Override
    public Class<Role> getEntityClass() {
        return Role.class;
    }
}
