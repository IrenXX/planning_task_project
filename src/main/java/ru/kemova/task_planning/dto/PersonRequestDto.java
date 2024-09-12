package ru.kemova.task_planning.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PersonRequestDto {
    private String name;
    private String email;
    private String password;
    private String confirmPassword;

}
