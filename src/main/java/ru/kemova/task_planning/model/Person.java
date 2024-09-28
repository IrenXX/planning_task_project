package ru.kemova.task_planning.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "person")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", nullable = false)
    @Schema(description = "Имя пользователя", example = "Jon")
    @NotBlank(message = "Имя пользователя не может быть пустыми")
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    @NotBlank(message = "Адрес электронной почты не может быть пустыми")
    @Email(message = "Email адрес должен быть в формате user@example.com")
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "Пароль не может быть пустыми")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Roles role;

    @OneToMany(mappedBy = "person")
    private List<Task> tasks;

//    public Person(String name, String email, String password, boolean confirmed, Roles role) {
//        this.name = name;
//        this.email = email;
//        this.password = password;
//        this.isConfirmed = confirmed;
//        this.status = Status.ACTIVE;
//        this.role = role;
//    }
}
