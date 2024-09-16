package ru.kemova.task_planning.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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
    private Long id;

    @Column(name = "name", nullable = false)
    @Schema(description = "Имя пользователя", example = "Jon")
    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Имя пользователя не может быть пустыми")
    private String name;

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

//
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "people_roles",
//            joinColumns = {@JoinColumn(name = "person_id", referencedColumnName = "id")},
//            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
//    )
//    private List<Role> roles;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Roles role;

    @OneToMany(mappedBy = "person")
    private List<Task> tasks;

    @Column(name = "confirmed")
    @Builder.Default
    private boolean isConfirmed = false;

    public Person(String name, String email, String password, boolean confirmed, Roles role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isConfirmed = confirmed;
        this.status = Status.ACTIVE;
        this.role = role;
    }
}
