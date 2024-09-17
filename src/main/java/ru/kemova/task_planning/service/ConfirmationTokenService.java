package ru.kemova.task_planning.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kemova.task_planning.model.ConfirmationToken;
import ru.kemova.task_planning.model.Person;
import ru.kemova.task_planning.repository.ConfirmationTokenRepository;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConfirmationTokenService {

    @Value("${app.token-confirm.lifetime}")
    private Duration tokenConfirmationLifeTime;

    private final ConfirmationTokenRepository repository;
    private final PersonService personService;

    @Transactional
    public ConfirmationToken createToken(Person person) {

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + tokenConfirmationLifeTime.toMillis());

        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(UUID.randomUUID())
                .person(person)
                .expired(expiredDate)
                .build();

        return repository.save(confirmationToken);
    }

    @Transactional(readOnly = true)
    public ConfirmationToken findToken(String token) {
        UUID uuid = UUID.fromString(token);
        return repository.findByToken(uuid).orElse(null);
    }

    @Transactional
    public boolean confirm(String token) {
        UUID uuidToken;
        try {
            uuidToken = UUID.fromString(token);
        } catch (IllegalArgumentException e) {
            return false;
        }

        ConfirmationToken confirmationToken = repository.findByToken(uuidToken).orElse(null);
        if (confirmationToken == null) {
            return false;
        }

        Person person = personService.findByEmail(confirmationToken.getPerson().getEmail());
        person.setConfirmed(true);
        personService.create(person);
        repository.delete(confirmationToken);
        return true;
    }

    @Transactional(readOnly = true)
    public ConfirmationToken findConfirmationTokenByPerson(Person person) {
        return repository.findByPerson(person).orElse(null);
    }
}