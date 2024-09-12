package ru.kemova.task_planning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kemova.task_planning.model.Person;
import ru.kemova.task_planning.model.ConfirmationToken;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findByToken(UUID token);
    Optional<ConfirmationToken> findByPerson(Person person);
}
