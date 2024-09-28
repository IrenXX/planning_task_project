package ru.kemova.task_planning.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kemova.task_planning.config.security.PersonDetails;
import ru.kemova.task_planning.dto.PersonRequestAuthnDto;
import ru.kemova.task_planning.dto.PersonRequestRegDto;
import ru.kemova.task_planning.dto.TokenResponseDto;
import ru.kemova.task_planning.exception.PasswordsNotSameException;
import ru.kemova.task_planning.exception.UserAlreadyExistException;
import ru.kemova.task_planning.model.Person;
import ru.kemova.task_planning.model.Roles;
import ru.kemova.task_planning.model.Status;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final PersonService personService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenService confirmationTokenService;
    private final BCryptPasswordEncoder passwordEncoder;

    public TokenResponseDto authenticate(PersonRequestAuthnDto personRequestAuthnDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(personRequestAuthnDto.getEmail(),
                            personRequestAuthnDto.getPassword()));
            log.info("User with username: {} - successfully authenticated", personRequestAuthnDto.getEmail());
        } catch (BadCredentialsException e) {
            log.info("User with username: {} - not authenticated", personRequestAuthnDto.getEmail());
            throw new BadCredentialsException("");
        }

        var person = Optional
                .of(personService.findByEmail(personRequestAuthnDto.getEmail()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found exception"));

        return TokenResponseDto.builder()
                .token(jwtService.generateToken(new PersonDetails(person)))
                .build();
    }

    public TokenResponseDto register(PersonRequestRegDto personRequestRegDto) {

        if (isPasswordsNotEqual(personRequestRegDto)) {
            throw new PasswordsNotSameException();
        }

        Person person = createPerson(personRequestRegDto);
        try {
            personService.create(person);
            log.info("User successfully saved in database -> {}, {}", personRequestRegDto.getUsername(),
                    personRequestRegDto.getEmail());
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistException();
        }

        var confirmationToken = confirmationTokenService.createToken(person);
        log.info("Token for user: {} - is produced", personRequestRegDto.getEmail());
//        try {
//            producerRabbitService.send(converterDtoService.createMessageDtoFromConfirmationToken(confirmationToken));
//            log.info("Token for user: {} - sent to rabbitMQ", personRequestRegDto.getEmail());
//        } catch (Exception e) {
//            log.info("Token for user: {} - DOESN'T sent to rabbitMQ", personRequestRegDto.getEmail());
//        }

        return TokenResponseDto.builder()
                .token(jwtService.generateToken(new PersonDetails(person)))
                .build();
    }

    private Person createPerson(PersonRequestRegDto personRequestRegDto) {
        return Person.builder()
                .username(personRequestRegDto.getUsername())
                .email(personRequestRegDto.getEmail())
                .password(passwordEncoder.encode(personRequestRegDto.getPassword()))
                .status(Status.ACTIVE)
                .role(Roles.ROLE_USER)
                .build();
    }

    private static boolean isPasswordsNotEqual(PersonRequestRegDto personRequestRegDto) {
        return !personRequestRegDto.getPassword().equals(personRequestRegDto.getConfirmPassword());
    }
}

