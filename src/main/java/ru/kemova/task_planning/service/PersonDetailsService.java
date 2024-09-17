package ru.kemova.task_planning.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kemova.task_planning.config.security.PersonDetails;
import ru.kemova.task_planning.model.Person;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonDetailsService implements UserDetailsService {
    private final PersonService personService;

    @Override
    @Transactional(readOnly=true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personService.findByEmail(username);
        log.info("Person with email: {} - successfully loaded for Security", username);
        return new PersonDetails(person);
    }
}
