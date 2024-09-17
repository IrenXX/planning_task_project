package ru.kemova.task_planning.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kemova.task_planning.dto.TaskRequestDto;
import ru.kemova.task_planning.dto.TaskResponseDto;
import ru.kemova.task_planning.exception.UserNotAuthenticatedException;
import ru.kemova.task_planning.exception.error.MessageError;
import ru.kemova.task_planning.exception.error.ProjectError;
import ru.kemova.task_planning.service.TaskService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@Slf4j
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAll(Principal principal) {
        String email = getEmailIfAuthenticated(principal);

        return ResponseEntity.ok(taskService.getTasksByEmail(email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getById(@PathVariable long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PostMapping
    public ResponseEntity<?> create(Principal principal, @RequestBody TaskRequestDto taskRequestDto) {
        String email = getEmailIfAuthenticated(principal);

        if (!taskService.save(email, taskRequestDto)) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ProjectError(HttpStatus.BAD_REQUEST.value(), MessageError.COULD_NOT_CREATE_ENTITY));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

    @PutMapping
    public ResponseEntity<?> update(Principal principal, @RequestBody TaskRequestDto taskRequestDto) {
        String email = getEmailIfAuthenticated(principal);

        if (!taskService.save(email, taskRequestDto)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ProjectError(HttpStatus.BAD_REQUEST.value(), MessageError.COULD_NOT_UPDATE_ENTITY));
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        taskService.delete(id);
    }

    private static String getEmailIfAuthenticated(Principal principal) {
        if (principal == null) {
            throw new UserNotAuthenticatedException();
        }
        return principal.getName();
    }
}
