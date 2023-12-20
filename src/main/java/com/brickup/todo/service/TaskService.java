package com.brickup.todo.service;

import java.text.ParseException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brickup.todo.dto.TaskRequestDTO;
import com.brickup.todo.entity.TaskEntity;
import com.brickup.todo.enums.SituationEnum;
import com.brickup.todo.exception.NotFoundException;
import com.brickup.todo.repository.TaskRepository;

@Service
public class TaskService {
	
	private TaskRepository taskRepository;
	
	public TaskService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	@Transactional(readOnly = true)
	public List<TaskEntity> findAllTask() {
		List<TaskEntity> taskList = this.taskRepository.findAll();
		
		return taskList;
	}

	@Transactional(readOnly = true)
	public TaskEntity findTaskById(Long id) throws NotFoundException {
		Optional<TaskEntity> taskEntity = this.taskRepository.findById(id);
		
		return taskEntity.orElseThrow(() -> new NotFoundException("Tarefa não encontrada!"));
	}
	
	@Transactional(readOnly = false)
	public TaskEntity createTask(TaskRequestDTO taskRequestDTO) throws ParseException {
		TaskEntity taskEntity = taskRequestDTO.convertToEntity();
		taskEntity.setSituation(SituationEnum.PENDING);
		
		this.taskRepository.save(taskEntity);
		
		return taskEntity;
	}
	
	@Transactional(readOnly = false)
	public TaskEntity updateTask(Long id, TaskRequestDTO taskRequestDTO) throws ParseException, NotFoundException {
		TaskEntity taskEntity = findTaskById(id);
		
		if(!Objects.isNull(taskRequestDTO.getDescription())) {
			taskEntity.setDescription(taskRequestDTO.getDescription()); 
		}
		taskEntity.setSituation(taskRequestDTO.getSituation()); 
		
		this.taskRepository.save(taskEntity);
		
		return taskEntity;
	}
	
	@Transactional(readOnly = false)
	public TaskEntity updateImagePathTask(Long id, String imagePath) throws ParseException, NotFoundException {
		TaskEntity taskEntity = findTaskById(id);
		taskEntity.setImagePath(imagePath);
		
		this.taskRepository.save(taskEntity);
		
		return taskEntity;
	}
	
}
