package com.cydeo.service;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;

import java.util.List;

public interface ProjectService extends CrudService<ProjectDTO,String>{
    void projectComplete(String projectCode);
    List<ProjectDTO> findByProjectDetail();
    List<ProjectDTO> listAllNotCompletedPrjByManager(User manager);

    List<Project> listAllProjectByManager(User manager);

    Boolean isProjectExist(ProjectDTO projectDTO);

    Boolean isValidStartEndDate(ProjectDTO projectDTO);
 }
