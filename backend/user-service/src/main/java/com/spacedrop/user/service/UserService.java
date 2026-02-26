package com.spacedrop.user.service;

import com.spacedrop.user.config.KafkaConfig;
import com.spacedrop.user.event.UserEvent;
import com.spacedrop.user.model.User;
import com.spacedrop.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    public UserService(UserRepository userRepository, KafkaTemplate<String, UserEvent> kafkaTemplate) {
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public User createUser(User user) {
        User saved = userRepository.save(user);
        UserEvent event = new UserEvent("USER_CREATED", saved.getId(), saved.getEmail(), saved.getRole().name());
        kafkaTemplate.send(KafkaConfig.USER_EVENTS_TOPIC, saved.getId().toString(), event);
        log.info("User created and event published: {}", saved.getEmail());
        return saved;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        log.info("User deleted: {}", id);
    }
}
