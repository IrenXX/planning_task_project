package ru.kemova.task_planning.dto;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class PersonResponseDto {
    private String username;
    private String email;
    private LocalDate registrationDate;
}
