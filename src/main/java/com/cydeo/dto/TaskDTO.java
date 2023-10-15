package com.cydeo.dto;

import com.cydeo.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

    private Long id;
    @NotNull(message = "choose project")
    private ProjectDTO project;
    @NotNull(message = "choose assign employee")
    private UserDTO assignedEmployee;
    @NotBlank(message = "task subject is required.")
    private String taskSubject;
    @NotBlank(message = "task detail is required.")
    private String taskDetail;
    private Status taskStatus;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate assignedDate;

}

