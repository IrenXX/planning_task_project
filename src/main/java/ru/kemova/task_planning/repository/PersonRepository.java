package ru.kemova.task_planning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kemova.task_planning.model.Person;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    Optional<Person> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
