package ru.kemova.task_planning.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDto {

    private long id;
    private String created;
    private String title;
    private String description;
    private boolean isDone;
}
