package com.emilyakavor.taskmanager.controller;

import com.emilyakavor.taskmanager.entity.Task;
import com.emilyakavor.taskmanager.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

        private final TaskService taskService;

        public TaskController(TaskService taskService) {
            this.taskService = taskService;
        }

        @GetMapping
        public ResponseEntity<List<Task>> getTasks(@RequestParam Optional<String> status,
                                                   @RequestParam Optional<Long> assignee) {
            return ResponseEntity.ok(taskService.getAllTasks(status, assignee));
        }

        @PostMapping
        public ResponseEntity<Task> createTask(@RequestBody Task task) {
            return ResponseEntity.ok(taskService.createTask(task));
        }

        @PutMapping("/{id}")
        public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
            return ResponseEntity.ok(taskService.updateTask(id, task));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<?> deleteTask(@PathVariable Long id) {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        }

}