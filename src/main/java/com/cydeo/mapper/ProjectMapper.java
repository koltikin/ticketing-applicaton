package com.cydeo.mapper;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.entity.Project;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper extends BaseMapper<ProjectDTO,Project>{

    public ProjectMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    public Class<ProjectDTO> getDtoClass() {
        return ProjectDTO.class;
    }

    @Override
    public Class<Project> getEntityClass() {
        return Project.class;
    }
}
