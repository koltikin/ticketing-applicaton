package com.cydeo.validations;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Component
@AllArgsConstructor
public class ProjectValidations {
    private final ProjectService projectService;
    public BindingResult addCustomValidations(ProjectDTO project, BindingResult bindingResult){

        if (projectService.isProjectExist(project)){
            bindingResult.addError(new FieldError("project","projectCode","Project Already Exist"));
        }
        if (projectService.isValidStartEndDate(project)){
            bindingResult.addError(new FieldError("project","projectEndDate","Project end date couldn't be before start date"));
        }

        return bindingResult;
    }

}
