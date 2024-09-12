package ru.kemova.task_planning.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kemova.task_planning.dto.PersonResponseDto;
import ru.kemova.task_planning.exception.UserNotAuthenticated;
import ru.kemova.task_planning.service.PersonService;
import ru.kemova.task_planning.service.ConverterDtoService;

import java.security.Principal;

@RestController
@RequestMapping(value =  "/api/v1/user")
@Slf4j
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;
    private final ConverterDtoService converterDtoService;

    @GetMapping
    public ResponseEntity<?> getPerson(Principal principal) {
        PersonResponseDto personResponseDto = null;
        if (principal != null) {
            personResponseDto = converterDtoService.getPersonFromDTO(personService.findByEmail(principal.getName()));
        } else {
            throw new UserNotAuthenticated();
        }

        return ResponseEntity.ok(personResponseDto);
    }
}
