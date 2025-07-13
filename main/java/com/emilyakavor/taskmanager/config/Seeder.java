package com.emilyakavor.taskmanager.config;

import com.emilyakavor.taskmanager.entity.Task;
import com.emilyakavor.taskmanager.entity.User;
import com.emilyakavor.taskmanager.repository.TaskRepository;
import com.emilyakavor.taskmanager.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class Seeder {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepo, TaskRepository taskRepo, PasswordEncoder passwordEncoder) {
        return args -> {
            if(userRepo.count() == 0) {
                // Seed users
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRole(User.Role.ADMIN);
                admin.setCreatedAt(LocalDateTime.now());

                User user = new User();
                user.setUsername("johndoe");
                user.setEmail("user@example.com");
                user.setPassword(passwordEncoder.encode("user"));
                user.setRole(User.Role.USER);
                user.setCreatedAt(LocalDateTime.now());

                userRepo.saveAll(List.of(admin, user));
                if(taskRepo.count() == 0) {

                    // Seed tasks
                    Task t1 = new Task("Design login UI", "Create a modern login screen", Task.Status.TODO, "High", user, admin);
                    Task t2 = new Task("Fix backend bug", "Resolve 500 error on /api/tasks", Task.Status.IN_PROGRESS, "Medium", user, admin);
                    Task t3 = new Task("Deploy to prod", "Deploy current build to production", Task.Status.DONE, "High", user, admin);
                    Task t4 = new Task("Write tests", "Add unit tests for service layer", Task.Status.TODO, "Low", user, admin);
                    Task t5 = new Task("Prepare docs", "Create onboarding docs for new devs", Task.Status.IN_PROGRESS, "Medium", user, admin);

                    taskRepo.saveAll(List.of(t1, t2, t3, t4, t5));
                }
            }
        };
    }
}

