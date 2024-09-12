package ru.kemova.task_planning.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class PersonRequestAuthnDto {
    private String email;
    private String password;

    @Override
    public String toString() {
        return "AuthenPersonDto{" +
                "email ='" + email + '\'' +
                ", password ='" + password + '\'' +
                '}';
    }
}
