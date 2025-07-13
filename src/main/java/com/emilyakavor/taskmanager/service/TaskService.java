package com.emilyakavor.taskmanager.service;

import com.emilyakavor.taskmanager.entity.Task;
import com.emilyakavor.taskmanager.entity.User;
import com.emilyakavor.taskmanager.repository.TaskRepository;
import com.emilyakavor.taskmanager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<Task> getAllTasks(Optional<String> status, Optional<Long> assigneeId) {
        List<Task> tasks = taskRepository.findAll();

        return tasks.stream()
                .filter(t -> status.map(s -> t.getStatus().toString().equalsIgnoreCase(s)).orElse(true))
                .filter(t -> assigneeId.map(id -> t.getAssignee() != null && t.getAssignee().getId().equals(id)).orElse(true))
                .collect(Collectors.toList());
    }

    public Task createTask(Task task) {
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        if (task.getAssignee() != null && task.getAssignee().getId() != null) {
            User assignee = userRepository.findById(task.getAssignee().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Assignee not found"));
            task.setAssignee(assignee);
        }

        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task updated) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        task.setTitle(updated.getTitle());
        task.setDescription(updated.getDescription());
        task.setStatus(updated.getStatus());
        task.setPriority(updated.getPriority());
        task.setUpdatedAt(java.time.LocalDateTime.now());

        if (updated.getAssignee() != null && updated.getAssignee().getId() != null) {
            User assignee = userRepository.findById(updated.getAssignee().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Assignee not found"));
            task.setAssignee(assignee);
        }

        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}