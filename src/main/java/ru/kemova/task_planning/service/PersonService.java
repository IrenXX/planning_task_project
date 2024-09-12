package ru.kemova.task_planning.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kemova.task_planning.model.Person;
import ru.kemova.task_planning.repository.PersonRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public List<Person> findAll() {
        List<Person> people = personRepository.findAll();
        log.info("All people successfully, size list -> {}", people.size());
        return people;
    }

    public Person findByEmail(String email) {
        Person person = personRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User with email: %s not found", email)));
        log.info("User with email: {} - successfully found", person.getEmail());
        return person;
    }

    public Person save(Person person) {
        return personRepository.save(person);
    }
}
