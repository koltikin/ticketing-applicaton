package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.Status;
import com.cydeo.service.ProjectService;
import com.cydeo.service.UserService;
import com.cydeo.validations.ProjectValidations;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("project")
public class ProjectControl {
    private final ProjectService projectService;
    private final UserService userService;
    private final ProjectValidations projectValidations;
    @PreAuthorize("hasAnyAuthority('Admin','Manager')")
    @GetMapping(value = "/create")
    public String projectCreate(Model model){
        model.addAttribute("project",new ProjectDTO());
        model.addAttribute("projectList", projectService.findByProjectDetail());
        model.addAttribute("managers", userService.findAllByRoleDetail());

        return "/project/create";
    }
    @PreAuthorize("hasAnyAuthority('Admin','Manager')")
    @PostMapping("/create")
    public String projectCreateSave(@Valid @ModelAttribute("project") ProjectDTO project, BindingResult bindingResult, Model model){

        bindingResult  = projectValidations.addCustomValidations(project,bindingResult);

        if (bindingResult.hasErrors()){

            model.addAttribute("projectList", projectService.findByProjectDetail());
            model.addAttribute("managers", userService.findAllByRoleDetail());

            return "/project/create";
        }


        projectService.save(project);


        return "redirect:/project/create";
    }

    @PreAuthorize("hasAnyAuthority('Admin','Manager')")
    @GetMapping("/complete/{projectCode}")
    public String projectComplete(@PathVariable("projectCode") String projectCode){

        projectService.projectComplete(projectCode);

        return "redirect:/project/create";
    }
    @PreAuthorize("hasAnyAuthority('Admin','Manager')")
    @GetMapping("/delete/{projectCode}")
    public String projectDelete(@PathVariable("projectCode") String projectCode){

        projectService.delete(projectCode);

        return "redirect:/project/create";
    }
    @PreAuthorize("hasAnyAuthority('Admin','Manager')")
    @GetMapping("/update/{projectCode}")
    public String projectUpdate(@PathVariable("projectCode") String projectCode,Model model){

        model.addAttribute("project",projectService.findById(projectCode));
        model.addAttribute("projectList", projectService.findByProjectDetail());
        model.addAttribute("managers", userService.findAllByRoleDetail());

        return "/project/update";
    }

    @PreAuthorize("hasAnyAuthority('Admin','Manager')")
    @PostMapping("/update/{projectStatus}")
    public String projectUpdateSave(@Valid @ModelAttribute("project") ProjectDTO project,BindingResult bindingResult, Model model){

        if (bindingResult.hasErrors()){

            model.addAttribute("projectList", projectService.findByProjectDetail());
            model.addAttribute("managers", userService.findAllByRoleDetail());
            return "/project/update";

        }

        projectService.update(project);

        return "redirect:/project/create";
    }

    @PreAuthorize("hasAuthority('Manager')")
    @GetMapping("/manager/project-status")
    public String projectStatus(Model model){

        model.addAttribute("projects", projectService.findByProjectDetail());

        return "/manager/project-status";
    }
    @PreAuthorize("hasAuthority('Manager')")
    @PostMapping("/manager/project-status/complete/{projectCode}")
    public String projectStatusComplete(@PathVariable("projectCode") String projectCode) {

        projectService.projectComplete(projectCode);


        return "redirect:/project/manager/project-status";
    }

}
