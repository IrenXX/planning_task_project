package ru.kemova.task_planning.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private String email;
    private String title;
    private String body;
}
