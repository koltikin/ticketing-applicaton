package com.cydeo.service;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskService extends CrudService<TaskDTO, Long>{

    int getAllCompletedTaskCount(String projectCode);
    int getAllUnfinishedTaskCount(String projectCode);

    List<TaskDTO> getAllTasksNotCompleted();

    List<TaskDTO> getAllCompletedTasks();

    void deleteTasksByProject(Project project);

    void updateTasksByProject(ProjectDTO projectDTO);

    List<TaskDTO> listAllNotCompletedTaskByEmployee(User employee);

    List<Task> listAllTasksByEmployee(User employee);

}
