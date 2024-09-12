package ru.kemova.task_planning.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kemova.task_planning.model.Role;
import ru.kemova.task_planning.repository.PersonRepository;
import ru.kemova.task_planning.repository.RoleRepository;
import ru.kemova.task_planning.repository.TaskRepository;

@Service
@RequiredArgsConstructor
public class StartPopulateDB {

    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final TaskRepository taskRepository;

    @PostConstruct
    public void startPopulate() {
        if (personRepository.findAll().isEmpty()
                && roleRepository.findAll().isEmpty()
                && taskRepository.findAll().isEmpty()) {
            Role userRole = new Role("ROLE_USER");
            Role adminRole = new Role("ROLE_ADMIN");
            // you should have this roles on start app
            userRole = roleRepository.save(userRole);
            adminRole = roleRepository.save(adminRole);

//            AppUser userUser = new AppUser("user", "user@1.ru", "$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i", true, List.of(userRole));
//            AppUser userUser1 = new AppUser("user_1", "user_1@1.ru", "$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i", true, List.of(userRole));
//            AppUser userUser2 = new AppUser("user_2", "user_2@1.ru", "$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i", true, List.of(userRole));
//            AppUser userUser3 = new AppUser("user_3", "user_3@1.ru", "$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i", false, List.of(userRole));
//            AppUser userAdmin = new AppUser("admin", "admin@1.ru", "$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i", false, List.of(adminRole));
//            userUser = appUserRepository.save(userUser);
//            userUser1 = appUserRepository.save(userUser1);
//            userUser2 = appUserRepository.save(userUser2);
//            userUser3 = appUserRepository.save(userUser3);
//            userAdmin = appUserRepository.save(userAdmin);
//
//            Task task1_1 = new Task("title_1", "description_1", TaskStatus.NEW, null, userUser1);
//            Task task1_2 = new Task("title_2", "description_2", TaskStatus.NEW, LocalDate.now().minusDays(2), userUser1);
//            Task task1_3 = new Task("title_3", "description_3", TaskStatus.DONE, LocalDate.now().minusDays(2), userUser1);
//            Task task2_1 = new Task("title_4", "description_4", TaskStatus.NEW, LocalDate.now(), userUser2);
//            Task task2_2 = new Task("title_5", "description_5", TaskStatus.NEW, LocalDate.now().minusDays(1), userUser2);
//            Task task2_3 = new Task("title_6", "description_6", TaskStatus.NEW, LocalDate.now().minusDays(2), userUser2);
//            Task task3_1 = new Task("title_7", "description_7", TaskStatus.NEW, LocalDate.now().minusDays(1), userUser2);
//            Task task3_2 = new Task("title_8", "description_8", TaskStatus.DONE, LocalDate.now().minusDays(3), userUser2);
//            Task task3_3 = new Task("title_9", "description_9", TaskStatus.DONE, LocalDate.now().minusDays(1), userUser2);
//            Task task3_4 = new Task("title_10", "description_10", TaskStatus.DONE, LocalDate.now().minusDays(1), userUser2);
//            Task task3_5 = new Task("title_11", "description_11", TaskStatus.DONE, LocalDate.now().minusDays(1), userUser2);
//            Task task3_6 = new Task("title_12", "description_12", TaskStatus.DONE, LocalDate.now().minusDays(4), userUser2);
//            List<Task> taskList = List.of(task1_1, task1_2, task1_3,
//                    task2_1, task2_2, task2_3,
//                    task3_1, task3_2, task3_3, task3_4, task3_5, task3_6);
//            taskRepository.saveAll(taskList);
        }
    }

}
