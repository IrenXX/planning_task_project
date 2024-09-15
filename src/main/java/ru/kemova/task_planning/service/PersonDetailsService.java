package ru.kemova.task_planning.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kemova.task_planning.config.security.PersonDetails;
import ru.kemova.task_planning.model.Person;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonDetailsService implements UserDetailsService {
    private final PersonService personService;

    @Override
    @Transactional
    public PersonDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Person person = personService.findByEmail(email);
        log.info("Person with email: {} - successfully loaded for Security", email);
        return new PersonDetails(person);
    }
}
