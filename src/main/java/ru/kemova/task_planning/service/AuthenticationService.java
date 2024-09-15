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
import ru.kemova.task_planning.dto.AuthnTokenResponseDto;
import ru.kemova.task_planning.dto.PersonRequestAuthnDto;
import ru.kemova.task_planning.dto.PersonRequestDto;
import ru.kemova.task_planning.model.Person;
import ru.kemova.task_planning.model.Status;
import ru.kemova.task_planning.exception.PasswordsNotSameException;
import ru.kemova.task_planning.exception.UserAlreadyExistException;
import ru.kemova.task_planning.repository.PersonRepository;
import ru.kemova.task_planning.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final String DEFAULT_ROLE = "ROLE_USER";
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenService confirmationTokenService;
    private final ConverterDtoService converterDtoService;
    private final ProducerRabbitService producerRabbitService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthnTokenResponseDto authenticate(PersonRequestAuthnDto personRequestAuthnDto) {
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
                .ofNullable(personRepository.findByEmail(personRequestAuthnDto.getEmail()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found exception"));

        return AuthnTokenResponseDto.builder()
                .token(jwtService.generateToken(new PersonDetails(person.get())))
                .build();
    }

    public AuthnTokenResponseDto register(PersonRequestDto personRequestDto) {

        if (isPasswordsNotEqual(personRequestDto)) {
            throw new PasswordsNotSameException();
        }

        Person person = createPerson(personRequestDto);
        try {
            personRepository.save(person);
            log.info("User successfully saved in database -> {}", personRequestDto.getEmail());
        } catch (DataIntegrityViolationException e) {
            if (e.getMostSpecificCause().getClass().getName().equals("org.postgresql.util.PSQLException")
                    && e.getMessage().contains("duplicate key value violates unique constraint")) {
                throw new UserAlreadyExistException();
            }
        }

        var confirmationToken = confirmationTokenService.createToken(person);
        log.info("Token for user: {} - is produced", personRequestDto.getEmail());
        try {
            producerRabbitService.send(converterDtoService.createConfirmMessageFromConfirmationToken(confirmationToken));
            log.info("Token for user: {} - sent to rabbitMQ", personRequestDto.getEmail());
        } catch (Exception e) {
            log.info("Token for user: {} - DOESN'T sent to rabbitMQ", personRequestDto.getEmail());
        }

        return AuthnTokenResponseDto.builder()
                .token(jwtService.generateToken(new PersonDetails(person)))
                .build();
    }

    private Person createPerson(PersonRequestDto personRequestDto) {
        return Person.builder()
                .name(personRequestDto.getName())
                .email(personRequestDto.getEmail())
                .password(passwordEncoder.encode(personRequestDto.getPassword()))
                .status(Status.ACTIVE)
                .roles(List.of(roleRepository.findByName(DEFAULT_ROLE)))
                .build();
    }

    private static boolean isPasswordsNotEqual(PersonRequestDto personRequestDto) {
        return !personRequestDto.getPassword().equals(personRequestDto.getConfirmPassword());
    }
}

