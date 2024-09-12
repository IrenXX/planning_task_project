package ru.kemova.task_planning.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kemova.task_planning.dto.TaskRequestDto;
import ru.kemova.task_planning.dto.TaskResponseDto;
import ru.kemova.task_planning.model.Person;
import ru.kemova.task_planning.model.Task;
import ru.kemova.task_planning.model.TaskStatus;
import ru.kemova.task_planning.repository.PersonRepository;
import ru.kemova.task_planning.repository.TaskRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;
    private final PersonRepository personRepository;
    private final ConverterDtoService converterDtoService;

    public List<TaskResponseDto> getTasksByEmail(String email) {

        Person person = personRepository.findByEmail(email).get();
        List<Task> tasks = taskRepository.findAllByPerson(person);
        List<TaskResponseDto> taskResponseDtoList = new ArrayList<>();
        if (tasks != null) {
            taskResponseDtoList = tasks.stream().map(converterDtoService::doFromTaskToTaskDto)
                    .collect(Collectors.toList());
        }
        return taskResponseDtoList;
    }


    public TaskResponseDto getTaskByIdAndEmail(long id, String email) {

        Person person = personRepository.findByEmail(email).orElseThrow();

        Task task = taskRepository.findByPersonAndId(person, id).orElse(null);
        if (task == null) {
            return new TaskResponseDto();
        }
        return converterDtoService.doFromTaskToTaskDto(task);
    }

    public boolean save(String email, TaskRequestDto taskRequestDto) {
        Person person = personRepository.findByEmail(email).orElse(null);
        if (person == null || taskRequestDto.getTitle() == null) {
            return false;
        }

        Task task;
        if (taskRequestDto.getId() == null) {
            task = Task.builder()
                    .person(person)
                    .title(taskRequestDto.getTitle())
                    .description(taskRequestDto.getDescription())
                    .taskStatus(taskRequestDto.getStatus() == null ? TaskStatus.NEW : TaskStatus.DONE)
                    .build();
            log.info("created task -> {}", task.toString());
        } else {
            task = taskRepository.findByPersonAndId(person, taskRequestDto.getId()).orElse(null);
            if (task == null) {
                return false;
            }
            task.setTitle(taskRequestDto.getTitle());
            task.setDescription(taskRequestDto.getDescription());

            if (taskRequestDto.getStatus() == null) {
                task.setTaskStatus(TaskStatus.NEW);
                task.setFinished(null);
            } else if (task.getTaskStatus() != TaskStatus.DONE) {
                task.setTaskStatus(TaskStatus.DONE);
                task.setFinished(LocalDate.now());
            }
        }

        taskRepository.save(task);
        return true;
    }

    public boolean delete(String email, long id) {
        Person person = personRepository.findByEmail(email).orElse(null);
        if (person == null) {
            return false;
        }

        Task task = taskRepository.findByPersonAndId(person, id).orElse(null);
        if (task == null) {
            return false;
        }
        taskRepository.delete(task);

        return true;
    }
}

