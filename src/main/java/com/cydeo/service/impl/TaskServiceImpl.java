package com.cydeo.service.impl;

import com.cydeo.Repository.TaskRepository;
import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskMapper mapper;
    private final TaskRepository repository;
    private final UserService userService;
    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;

    @Override
    public List<TaskDTO> findAll() {
        return repository.findAll(Sort.by(Sort.Order.desc("assignedDate"))).stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO findById(Long id) {
        Optional<Task> task = repository.findById(id);
        if (task.isPresent()){
            return mapper.convertToDto(task.get());
        }
        return null;
    }

    @Override
    public void save(TaskDTO dto) {
        repository.save(mapper.convertToEntity(dto));

    }

    @Override
    public void update(TaskDTO dto) {
        Long task_id = dto.getId();
        Optional<Task> task = repository.findById(task_id);
        task.ifPresent(tk -> tk.setTaskStatus(dto.getTaskStatus()));
        repository.save(task.get());

    }

    @Override
    public void delete(Long id) {
        Optional<Task> task = repository.findById(id);
        if (task.isPresent()) {
            task.get().setIsDeleted(true);
            repository.save(task.get());
        }

    }

    @Override
    public int getAllCompletedTaskCount(String projectCode) {
        return repository.totalCompletedTaskCount(projectCode);
    }

    @Override
    public int getAllUnfinishedTaskCount(String projectCode) {
        return repository.totalUnfinishedTaskCount(projectCode);
    }

    @Override
    public List<TaskDTO> getAllTasksNotCompleted() {
        UserDTO loggedInUser = userService.findById("john@employee.com");
        User user = userMapper.convertToEntity(loggedInUser);
        return repository.findAllByTaskStatusIsNotAndAssignedEmployee(Status.COMPLETE, user).stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getAllCompletedTasks() {
        UserDTO loggedInUser = userService.findById("john@employee.com");
        User user = userMapper.convertToEntity(loggedInUser);
        return repository.findAllByTaskStatusIsAndAssignedEmployee(Status.COMPLETE, user).stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());

    }

    @Override
    public void deleteTasksByProject(Project project) {
        List<Task> tasks = repository.findAllByProject(project);

        tasks.forEach(task -> repository.deleteById(task.getId()));

    }

    @Override
    public void updateTasksByProject(ProjectDTO projectDTO) {
        List<Task> tasks = repository.findAllByProject(projectMapper.convertToEntity(projectDTO));
        tasks.stream().map(mapper::convertToDto).forEach(task -> {
            task.setTaskStatus(Status.COMPLETE);
            update(task);
        });
    }

    @Override
    public List<TaskDTO> listAllNotCompletedTaskByEmployee(User employee) {
        List<Task> tasks = repository.findAllByAssignedEmployeeAndTaskStatusNot(employee, Status.COMPLETE);
        return tasks.stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> listAllTasksByEmployee(User employee) {
        return repository.findAllByAssignedEmployee(employee);
    }
}
