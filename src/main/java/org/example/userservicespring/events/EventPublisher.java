package org.example.userservicespring.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;
    @Value("{app.kafka.topic.user-events}")
    private String topic;
    public void publishCreateUserEvent(String email) {
        publish(new UserEvent(UserEventType.CREATED, email));
    }
    public void publishDeleteUserEvent(String email) {
        publish(new UserEvent(UserEventType.DELETED, email));
    }
    private void publish(UserEvent userEvent) {
        kafkaTemplate.send(topic, userEvent);
    }
}
