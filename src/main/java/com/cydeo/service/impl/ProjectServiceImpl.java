package com.cydeo.service.impl;

import com.cydeo.Repository.ProjectRepository;
import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectMapper mapper;
    private final ProjectRepository repository;
    private final UserService userService;
    private final UserMapper  userMapper;
    private final TaskService taskService;
    @Override
    public List<ProjectDTO> findAll() {
        return (repository.findAll(Sort.by("projectName"))).stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDTO findById(String projectCode) {
        return mapper.convertToDto(repository.findByProjectCode(projectCode));
    }

    @Override
    public void save(ProjectDTO dto) {
//        if (repository.findByProjectCode(dto.getProjectCode()) == null) {
            Project project = mapper.convertToEntity(dto);
            project.setProjectStatus(Status.OPEN);
            repository.save(project);
//        }

    }

    @Override
    public void update(ProjectDTO dto) {
        Long old_proj_id = repository.findByProjectCode(dto.getProjectCode()).getId();
        Project updtedProject = mapper.convertToEntity(dto);
        updtedProject.setId(old_proj_id);
        repository.save(updtedProject);


    }

    @Override
    public void delete(String projectCode) {
        Project delProject  = repository.findByProjectCode(projectCode);

        delProject.setProjectCode(projectCode +"-"+ LocalDateTime.now());
        delProject.setIsDeleted(true);

        repository.save(delProject);

        taskService.deleteTasksByProject(delProject);



    }

    @Override
    public void projectComplete(String projectCode) {
        Project project = repository.findByProjectCode(projectCode);
        project.setProjectStatus(Status.COMPLETE);
        repository.save(project);

        taskService.updateTasksByProject(mapper.convertToDto(project));

    }

    @Override
    public List<ProjectDTO> findByProjectDetail() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Project> projects;
        UserDTO currentUser = userService.findById(userName);
        if (currentUser.getRole().getDescription().equals("Admin")){
             projects = repository.findAll();
        }
        else {
            User currentManager = userMapper.convertToEntity(currentUser);
             projects = repository.findByProjectManager(currentManager);
        }

        return  projects.stream().map(prj->{
            ProjectDTO prjDTO = mapper.convertToDto(prj);

            prjDTO.setCompletedCount(taskService.getAllCompletedTaskCount(prjDTO.getProjectCode()));
            prjDTO.setUnfinishedCount(taskService.getAllUnfinishedTaskCount(prjDTO.getProjectCode()));

            return prjDTO;
        }).collect(Collectors.toList());

    }

    @Override
    public List<ProjectDTO> listAllNotCompletedPrjByManager(User manager) {
        List<Project> projects =  repository.findAllByProjectManagerAndProjectStatusNot(manager,Status.COMPLETE);

        return projects.stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Project> listAllProjectByManager(User manager) {
        return repository.findAllByProjectManager(manager);
    }

    @Override
    public Boolean isProjectExist(ProjectDTO projectDTO) {
        return repository.existsByProjectCode(projectDTO.getProjectCode());
    }

    @Override
    public Boolean isValidStartEndDate(ProjectDTO project) {
        return !project.getProjectEndDate().isAfter(project.getProjectStartDate());
    }
}
