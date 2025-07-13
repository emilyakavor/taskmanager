package com.emilyakavor.taskmanager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "tasks")
@Table(name = "tasks")
@Data
@NoArgsConstructor
public class Task {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;
    private String priority;

    @ManyToOne
    private User assignee;

    @ManyToOne
    private User creator;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Task(String title, String description, Status status, String priority, User assignee, User creator) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.assignee = assignee;
        this.creator = creator;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public enum Status {
        TODO, IN_PROGRESS, DONE
    }
}