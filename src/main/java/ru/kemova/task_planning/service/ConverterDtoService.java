package ru.kemova.task_planning.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;
import ru.kemova.task_planning.dto.PersonResponseDto;
import ru.kemova.task_planning.dto.TaskResponseDto;
import ru.kemova.task_planning.model.Person;
import ru.kemova.task_planning.model.Task;
import ru.kemova.task_planning.model.TaskStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConverterDtoService {


    private final ModelMapper modelMapper;

    public PersonResponseDto getPersonFromDTO(Person person) {
        TypeMap<Person, PersonResponseDto> typeMap = modelMapper.typeMap(Person.class, PersonResponseDto.class);
        Converter<Long, LocalDate> toLocalDate = new AbstractConverter<Long, LocalDate>() {
            @Override
            protected LocalDate convert(Long source) {
                LocalDate date =
                        Instant.ofEpochMilli(source).atZone(ZoneId.systemDefault()).toLocalDate();
                return date;
            }
        };
        typeMap.addMappings(mapping -> mapping.using(toLocalDate).map(null, PersonResponseDto::setRegistrationDate));

        return modelMapper.map(person, PersonResponseDto.class);
    }

    public TaskResponseDto getTaskDtoFromTask(Task task) {

        TypeMap<Task, TaskResponseDto> typeMap = modelMapper.typeMap(Task.class, TaskResponseDto.class);

        Converter<Long, String> toLocalDate = new AbstractConverter<Long, String>() {
            @Override
            protected String convert(Long source) {
                LocalDateTime dateTime =
                        Instant.ofEpochMilli(source).atZone(ZoneId.systemDefault()).toLocalDateTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                return dateTime.format(formatter);
            }
        };
        Converter<TaskStatus, Boolean> isDone = new AbstractConverter<TaskStatus, Boolean>() {
            protected Boolean convert(TaskStatus taskStatus) {
                return taskStatus == TaskStatus.DONE;
            }
        };
        typeMap.addMappings(mapping -> mapping.using(toLocalDate).map(Task::getCreateAt, TaskResponseDto::setCreated));
        typeMap.addMappings(mapping -> mapping.using(isDone).map(Task::getTaskStatus, TaskResponseDto::setDone));

        return modelMapper.map(task, TaskResponseDto.class);
    }
}
