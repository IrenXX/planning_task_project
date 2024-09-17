package ru.kemova.task_planning.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Schema(description = "Запрос на регистрацию")
public class PersonRequestRegDto {

    @Schema(description = "Имя пользователя", example = "Jon")
    @Size(min = 2, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Имя пользователя не может быть пустыми")
    private String name;

    @Schema(description = "Адрес электронной почты", example = "jondoe@gmail.com")
    @Size(min = 2, max = 255, message = "Адрес электронной почты должен содержать от 5 до 255 символов")
    @NotBlank(message = "Адрес электронной почты не может быть пустыми")
    @Email(message = "Email адрес должен быть в формате user@example.com")
    private String email;

    @Schema(description = "Пароль", example = "my_1secret1_password")
    @Size(min = 2, max = 255, message = "Длина пароля должна быть не более 255 символов")
    private String password;

    private String confirmPassword;
}
