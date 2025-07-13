package com.emilyakavor.taskmanager.repository;

import com.emilyakavor.taskmanager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
