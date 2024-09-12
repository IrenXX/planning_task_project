package ru.kemova.task_planning.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kemova.task_planning.dto.AuthnResponseDto;
import ru.kemova.task_planning.dto.PersonRequestAuthnDto;
import ru.kemova.task_planning.dto.PersonRequestDto;
import ru.kemova.task_planning.service.AuthenticationService;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

//    @Operation(
//            summary = "Аутентификация пользователя",
//            description = "Позволяет получить токен доступа отправив пользовательские данные"
//    )
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Успешная аутентификация",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = PersonDto.class))),
//            @ApiResponse(responseCode = "400", description = "Ошибка валидации",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = AppError.class))),
//            @ApiResponse(responseCode = "401", description = "Неавторизован",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = AppError.class))),
//            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = AppError.class)))
//    })
    @PostMapping("/authenticate")
    public ResponseEntity<?> generateJwtToken(@RequestBody @Valid PersonRequestAuthnDto personRequestAuthnDto) {
        return ResponseEntity.ok(authenticationService.authenticate(personRequestAuthnDto));
    }

//    @Operation(
//            summary = "Регистрация пользователя",
//            description = "Позволяет зарегистрировать пользователя"
//    )
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Успешная регистрация",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = PersonDto.class))),
//            @ApiResponse(responseCode = "400", description = "Ошибка валидации",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = AppError.class))),
//            @ApiResponse(responseCode = "409", description = "Пользователь уже существует",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = AppError.class))),
//            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = AppError.class)))
//    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid PersonRequestDto personRequestDto) {
        log.info("Request for register user -> {}", personRequestDto.getEmail());
        AuthnResponseDto registered = authenticationService.register(personRequestDto);
        log.info("User successfully registered -> {}", personRequestDto.getEmail());
        return ResponseEntity.ok(registered);
    }
}
