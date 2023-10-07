package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.Status;
import com.cydeo.service.ProjectService;
import com.cydeo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("project")
public class ProjectControl {
    private final ProjectService projectService;
    private final UserService userService;
    @GetMapping("/create")
    public String projectCreate(Model model){
        model.addAttribute("project",new ProjectDTO());
        model.addAttribute("projectList", projectService.findByProjectDetail());
        model.addAttribute("managers", userService.findAllByRole("Manager"));

        return "/project/create";
    }

    @PostMapping("/create")
    public String projectCreateSave(@Valid @ModelAttribute("project") ProjectDTO project, BindingResult bindingResult, Model model){

        if (bindingResult.hasErrors()){

            model.addAttribute("projectList", projectService.findByProjectDetail());
            model.addAttribute("managers", userService.findAllByRole("Manager"));

            return "/project/create";
        }


        projectService.save(project);


        return "redirect:/project/create";
    }


    @GetMapping("/complete/{projectCode}")
    public String projectComplete(@PathVariable("projectCode") String projectCode){

        projectService.projectComplete(projectCode);

        return "redirect:/project/create";
    }

    @GetMapping("/delete/{projectCode}")
    public String projectDelete(@PathVariable("projectCode") String projectCode){

        projectService.delete(projectCode);

        return "redirect:/project/create";
    }

    @GetMapping("/update/{projectCode}")
    public String projectUpdate(@PathVariable("projectCode") String projectCode,Model model){

        model.addAttribute("project",projectService.findById(projectCode));
        model.addAttribute("projectList", projectService.findByProjectDetail());
        model.addAttribute("managers", userService.findAllByRole("manager"));

        return "/project/update";
    }


    @PostMapping("/update/{projectStatus}")
    public String projectUpdateSave(@Valid @ModelAttribute("project") ProjectDTO project,BindingResult bindingResult, Model model){

        if (bindingResult.hasErrors()){

            model.addAttribute("projectList", projectService.findByProjectDetail());
            model.addAttribute("managers", userService.findAllByRole("manager"));
            return "/project/update";

        }

        projectService.update(project);

        return "redirect:/project/create";
    }
//
    @GetMapping("/manager/project-status")
    public String projectStatus(Model model){

        model.addAttribute("projects", projectService.findByProjectDetail());

        return "/manager/project-status";
    }
    @GetMapping("/manager/project-status/complete/{projectCode}")
    public String projectStatusComplete(@PathVariable("projectCode") String projectCode) {

        projectService.projectComplete(projectCode);


        return "redirect:/project/manager/project-status";
    }

}
