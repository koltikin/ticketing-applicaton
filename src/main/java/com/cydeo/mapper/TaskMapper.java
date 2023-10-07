package com.cydeo.mapper;

import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Task;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper extends BaseMapper<TaskDTO, Task>{


    public TaskMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    public Class<TaskDTO> getDtoClass() {
        return TaskDTO.class;
    }

    @Override
    public Class<Task> getEntityClass() {
        return Task.class;
    }
}
