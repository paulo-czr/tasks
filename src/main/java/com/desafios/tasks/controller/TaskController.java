package com.desafios.tasks.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.desafios.tasks.entity.Task;
import com.desafios.tasks.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    
    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity <Task> create(@RequestBody Task task){
        Task created = taskService.create(task);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public ResponseEntity <List<Task>> listAll(){
        List<Task> tasks = taskService.listAll();

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity <Task> findById(@PathVariable Long id){
        Task task = taskService.findById(id);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity <Task> updateTask(@PathVariable Long id, @RequestBody Task existingTask){
        Task task = taskService.updateTask(id, existingTask);
        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity <Task> updateStatus(@PathVariable Long id, @RequestParam boolean status){
        Task task = taskService.updateStatus(id, status);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity <Void> deleteById(@PathVariable Long id){
        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
