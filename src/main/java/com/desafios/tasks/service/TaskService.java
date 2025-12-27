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
        securityCheck(task);

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
        securityCheck(updatedTask);

        Task existingTask = taskRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Tarefa não encontrada para atualização."));

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

    /**
     * Verificação de segurança
     * @param task a Tarefa a ser verificada
     */
    public void securityCheck(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Os dados da Tarefa não podem ser nulos.");
        }

        String title = task.getTitle();
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("O Título é obrigatório.");
        }

        task.setTitle(title.trim());
        if (task.getTitle().length() < 3 || task.getTitle().length() > 150) {
            throw new IllegalArgumentException("O título deve ter entre 3 e 150 caracteres.");
        }
        
        if (task.getDescription() != null) {
            task.setDescription(task.getDescription().trim());
            if (task.getDescription().length() > 700) {
                throw new IllegalArgumentException("A descrição é muito longa.");
            }
        }
    }

}
