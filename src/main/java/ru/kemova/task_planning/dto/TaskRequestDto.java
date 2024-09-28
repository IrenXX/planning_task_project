package ru.kemova.task_planning.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class TaskRequestDto {
    private int id;
    private String title;
    private String description;
    private String status;
}
