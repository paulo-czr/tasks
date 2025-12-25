package com.desafios.tasks.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.desafios.tasks.entity.Task;
import com.desafios.tasks.repository.TaskRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Cria a Tarefa
     * 
     * @param task A Tarefa a ser criada
     * @return retorna a tarefa que foi criada
     */
    @Transactional  
    public Task create(Task task) {
        if (task.getId() != null) {
            throw new IllegalArgumentException("Task não pode ter ID.");
        }

        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new IllegalArgumentException("Título é obrigatório");
        }

        return taskRepository.save(task);
    }

    /**
     * Listagem de todas as tarefas
     * 
     * @return Lista com todas as tarefas
     */
    public List<Task> listAll() {
        return taskRepository.findAll();
    }

    /**
     * Lista somente uma tarefa
     * 
     * @param id Id da tarefa a ser listada
     * @return A tarefa com o ID selecionado
     */
    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Tarefa não encontrada."));
    }

    /**
     * Atualiza a Tarefa
     * 
     * @param id          A tarfea a ser atualizada
     * @param updatedTask A nova tarefa com as informações que serão atualizadas
     * @return A tarefa atualizada
     */
    @Transactional
    public Task updateTask(Long id, Task updatedTask) {
        Task existingTask = taskRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Tarefa não encontrada para atualização."));

        if (updatedTask.getTitle() == null || updatedTask.getTitle().isBlank()) {
            throw new IllegalArgumentException("Título é obrigatório");
        }

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setCompleted(updatedTask.isCompleted());

        return taskRepository.save(existingTask);

    }

    /**
     * Atualiza o Status da Tarefa
     * 
     * @param id     ID da Tarefa a ser atualizada
     * @param status True para marcar como concluída, False para marcar como
     *               Pendente
     * @return Task atualizada
     */
    @Transactional
    public Task updateStatus(Long id, boolean status) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Tarefa não encontrada para atualização."));

        task.setCompleted(status);
        return taskRepository.save(task);
    }

    /**
     * Remove a Tarefa
     * 
     * @param id ID da tarefa a ser removida
     */
    @Transactional
    public void deleteById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new EntityNotFoundException("Tarefa com o ID " + id + " não encontrada.");
        }

        taskRepository.deleteById(id);
    }
}
