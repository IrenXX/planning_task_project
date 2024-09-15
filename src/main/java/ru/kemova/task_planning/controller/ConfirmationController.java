package ru.kemova.task_planning.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kemova.task_planning.config.security.PersonDetails;
import ru.kemova.task_planning.dto.AuthnTokenResponseDto;
import ru.kemova.task_planning.model.Person;
import ru.kemova.task_planning.model.ConfirmationToken;
import ru.kemova.task_planning.exception.ConfirmationNotSuccessfullyException;
import ru.kemova.task_planning.service.*;

import java.security.Principal;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/confirmation-email")
public class ConfirmationController {

    private final ConfirmationTokenService confirmationTokenService;
    private final PersonService personService;
    private final ProducerRabbitService producerRabbitService;
    private final ConverterDtoService converterDtoService;
    private final JwtService jwtService;

    @GetMapping
    public ResponseEntity<?> confirm(@RequestParam("token") String token, Principal principal) {
        log.info("confirm token taken -> {}", token);
        if (!confirmationTokenService.confirm(token)) {
            throw new ConfirmationNotSuccessfullyException();
        }

        Person person = personService.findByEmail(principal.getName());
        AuthnTokenResponseDto confirmed = AuthnTokenResponseDto.builder()
                .token(jwtService.generateToken(new PersonDetails(person)))
                .build();

        return ResponseEntity.ok(confirmed);
    }

    @GetMapping("/send-again")
    public ResponseEntity<?> sendConfirm(Principal principal) {
        log.info("Try send confirm email one more time, for user -> {}", principal.getName());
        ConfirmationToken confirmationToken = confirmationTokenService.findConfirmationTokenByPerson(personService.findByEmail(principal.getName()));
        if (confirmationToken == null) {
            throw new ConfirmationNotSuccessfullyException();
        }
        producerRabbitService.send(converterDtoService.createConfirmMessageFromConfirmationToken(confirmationToken));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }
}
