package ru.kemova.task_planning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kemova.task_planning.model.Person;
import ru.kemova.task_planning.model.Task;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByPerson(Person person);
    Optional<Task> findById(Long id);
}
