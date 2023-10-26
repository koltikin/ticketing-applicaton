package com.cydeo.Repository;

import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {

    Project findByProjectCode(String projectCode);

    List<Project> findByProjectManager(User manager);
    List<Project> findAllByProjectManagerAndProjectStatusNot(User manager, Status status);


    List<Project> findAllByProjectManager(User manager);

    Boolean existsByProjectCode(String projectCode);
}
