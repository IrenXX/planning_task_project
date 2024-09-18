package ru.kemova.task_planning.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kemova.task_planning.config.security.PersonDetails;
import ru.kemova.task_planning.dto.PersonRequestAuthnDto;
import ru.kemova.task_planning.dto.PersonRequestRegDto;
import ru.kemova.task_planning.dto.TokenResponseDto;
import ru.kemova.task_planning.exception.ConfirmationNotSuccessfullyException;
import ru.kemova.task_planning.model.ConfirmationToken;
import ru.kemova.task_planning.model.Person;
import ru.kemova.task_planning.service.*;

import java.security.Principal;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final PersonService personService;
    private final ConfirmationTokenService confirmationTokenService;
    private final JwtService jwtService;

    @Operation(
            summary = "Аутентификация пользователя",
            description = "Позволяет получить токен доступа отправив пользовательские данные"
    )
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
    public ResponseEntity<TokenResponseDto> generateJwtToken(@RequestBody @Valid PersonRequestAuthnDto personRequestAuthnDto) {
        return ResponseEntity.ok(authenticationService.authenticate(personRequestAuthnDto));
    }

    @Operation(
            summary = "Регистрация пользователя",
            description = "Позволяет зарегистрировать пользователя"
    )
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
    public ResponseEntity<TokenResponseDto> registerUser(@RequestBody @Valid PersonRequestRegDto personRequestRegDto) {
        log.info("Request for register user -> {}", personRequestRegDto.getEmail());
        TokenResponseDto registered = authenticationService.register(personRequestRegDto);
        log.info("User successfully registered -> {}", personRequestRegDto.getEmail());
        return ResponseEntity.ok(registered);
    }

    @GetMapping("/confirmation-email")
    public ResponseEntity<TokenResponseDto> confirm(@RequestParam("token") String token, Principal principal) {
        log.info("confirm token taken -> {}", token);
        if (!confirmationTokenService.confirm(token)) {
            throw new ConfirmationNotSuccessfullyException();
        }

        Person person = personService.findByEmail(principal.getName());
        TokenResponseDto confirmed = TokenResponseDto.builder()
                .token(jwtService.generateToken(new PersonDetails(person)))
                .build();

        return ResponseEntity.ok(confirmed);
    }

    @GetMapping("/send-again")
    public ResponseEntity<?> sendConfirm(Principal principal) {
        log.info("Try send confirm email one more time, for user -> {}", principal.getName());
        ConfirmationToken confirmationToken = confirmationTokenService
                .findConfirmationTokenByPerson(personService.findByEmail(principal.getName()));
        if (confirmationToken == null) {
            throw new ConfirmationNotSuccessfullyException();
        }
       // emailTaskProducer.send(taskService.createMessageDtoFromConfirmationToken(confirmationToken));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }
}
