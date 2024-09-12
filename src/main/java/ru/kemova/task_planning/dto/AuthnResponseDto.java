package ru.kemova.task_planning.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class AuthnResponseDto {
    private String token;
}
