package ru.kemova.task_planning.exception.error;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProjectError {

    private int status;
    private MessageError message;
}
