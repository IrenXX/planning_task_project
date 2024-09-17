package ru.kemova.task_planning.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kemova.task_planning.dto.PersonResponseDto;
import ru.kemova.task_planning.exception.UserNotAuthenticatedException;
import ru.kemova.task_planning.model.Person;
import ru.kemova.task_planning.service.ConverterDtoService;
import ru.kemova.task_planning.service.PersonService;

import java.security.Principal;

@RestController
@RequestMapping(value = "/api/v1/user")
@Slf4j
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;
    private final ConverterDtoService converterDtoService;

    @GetMapping
    public ResponseEntity<PersonResponseDto> getPerson(Principal principal) {
        Person person = personService.findByEmail(principal.getName());
        PersonResponseDto personResponseDto = converterDtoService.getPersonFromDTO(person);
        return ResponseEntity.ok(personResponseDto);
    }
}
