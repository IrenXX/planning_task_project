package ru.kemova.task_planning.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kemova.task_planning.exception.UserAlreadyExistException;
import ru.kemova.task_planning.model.Person;
import ru.kemova.task_planning.repository.PersonRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    //@Transactional
    public List<Person> findAll() {
        List<Person> people = personRepository.findAll();
        log.info("All people successfully, size list -> {}", people.size());
        return people;
    }

    public Person findByEmail(String email) {
        Person person = personRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format("Person with email: %s not found", email)));
        log.info("Person with email: {} - successfully found", person.getEmail());
        return person;
    }

   // @Transactional
    public Person save(Person person) {
        return personRepository.save(person);
    }

    //@Transactional
    public Person create(Person person) {
        log.info("Process for create person start");
        if (personRepository.existsByName(person.getName())) {
            log.info("Person with name: {} - already exist", person.getName());
            throw new UserAlreadyExistException();
        }
        if (personRepository.existsByEmail(person.getEmail())) {
            log.info("Person with email: {} - already exist", person.getEmail());
            throw new UserAlreadyExistException();
        }
        return save(person);
    }
}
