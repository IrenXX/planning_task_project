package ru.kemova.task_planning.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kemova.task_planning.dto.MessageDto;
import ru.kemova.task_planning.dto.TaskRequestDto;
import ru.kemova.task_planning.dto.TaskResponseDto;
import ru.kemova.task_planning.model.ConfirmationToken;
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

    //@Value("${app.public-host}")
    private String publicHost;

    private final TaskRepository taskRepository;
    private final PersonService personService;
    private final ConverterDtoService converterDtoService;

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getTasksByEmail(String email) {
        Person person = personService.findByEmail(email);
        List<Task> tasks = taskRepository.findAllByPerson(person);
        List<TaskResponseDto> taskResponseDtoList = new ArrayList<>();
        if (tasks != null) {
            taskResponseDtoList = tasks.stream().map(converterDtoService::getTaskDtoFromTask)
                    .collect(Collectors.toList());
        }
        return taskResponseDtoList;
    }

    @Transactional(readOnly = true)
    public TaskResponseDto getTaskById(long id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task == null) {
            return new TaskResponseDto();
        }
        return converterDtoService.getTaskDtoFromTask(task);
    }

    @Transactional
    public boolean save(String email, TaskRequestDto taskRequestDto) {
        Person person = personService.findByEmail(email);
        if (taskRequestDto.getTitle() == null) {
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
            task = taskRepository.findById(taskRequestDto.getId()).orElse(null);
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

    @Transactional
    public void delete(long id) {
        Task task = taskRepository.findById(id).orElseThrow(()->
                new UsernameNotFoundException(String.format("Task not found with id %d",id)));
        taskRepository.delete(task);
    }

    public MessageDto createMessageDtoFromConfirmationToken(ConfirmationToken confirmationToken) {
        var title = String.format("Confirm registration on %s", publicHost);
        var body = String.format("To confirm the email, please follow the link from the email <a href=\"%s/?confirm-token=%s\">%s/?confirm-token=%s</a>",
                publicHost, confirmationToken.getToken(), publicHost, confirmationToken.getToken());
        return MessageDto.builder()
                .email(confirmationToken.getPerson().getEmail())
                .title(title)
                .body(body)
                .build();
    }
}

