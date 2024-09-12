package ru.kemova.task_planning.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "confirm_tokens")
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "token")
    private UUID token;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Person person;

    @Column(name = "expired")
    private Date expired;
}
