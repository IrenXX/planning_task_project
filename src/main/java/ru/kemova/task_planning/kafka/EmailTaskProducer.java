package ru.kemova.task_planning.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.kemova.task_planning.dto.EmailMessageTask;

@Component
@RequiredArgsConstructor
public class EmailTaskProducer {
    private final KafkaTemplate <String, EmailMessageTask> kafkaTemplate;

    public void sendEmailTask(EmailMessageTask emailTask) {
        kafkaTemplate.send("EMAIL_SENDING_TASKS", emailTask);
    }
}