package com.brickup.todo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.brickup.todo.dto.TaskRequestDTO;
import com.brickup.todo.dto.TaskResponseDTO;
import com.brickup.todo.entity.TaskEntity;
import com.brickup.todo.enums.SituationEnum;
import com.brickup.todo.exception.NotFoundException;
import com.brickup.todo.exception.WarningException;
import com.brickup.todo.service.TaskService;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

	@Autowired
	private Environment env;
	
	private TaskService taskService;
	
	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}
	
	@PostMapping
	public ResponseEntity<TaskResponseDTO> createPerson(@RequestBody TaskRequestDTO taskRequestDTO) throws ParseException, WarningException, IOException {
		TaskEntity taskEntity = this.taskService.createTask(taskRequestDTO);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(TaskResponseDTO.convertToDTO(taskEntity));
	}
	
	@GetMapping(value = "/{id}/image", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public @ResponseBody byte[] getFile(@PathVariable long id) throws IOException, NotFoundException, WarningException {
		TaskEntity taskEntity = this.taskService.findTaskById(id);
		String imagePath = taskEntity.getImagePath();
		
		if(Objects.isNull(imagePath)) throw new WarningException("The task does not have an image!");
		
		File file = new File(imagePath);   
		
		InputStream targetStream = new FileInputStream(file);
		byte[] bites = targetStream.readAllBytes();
		targetStream.close();
		
	    return bites;
	}
	
	@PostMapping("/{id}/upload")
	public ResponseEntity<String> uploadTaskImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws ParseException, WarningException, IOException, NotFoundException {
		if(Objects.isNull(file)) throw new WarningException("File is required!");
		
		String fileExtension = extractExtension(file.getOriginalFilename()).toLowerCase();
		
		String fileName = UUID.randomUUID() + "." + fileExtension;

		String[] validExtensions = new String[] {"jpg", "png", "bmp", "gif"};
		if(!Arrays.asList(validExtensions).contains(fileExtension)) {
			throw new WarningException("File format is invalid!");
		}
		
		String filePath = env.getProperty("file.path") + "/" + fileName;

		
		Files.copy(file.getInputStream(), Path.of(filePath), StandardCopyOption.REPLACE_EXISTING);
		this.taskService.updateImagePathTask(id, filePath);
		
		return ResponseEntity.status(HttpStatus.CREATED).body("{ \"message\": \"File uploaded!\"}");
	}
	
	private String extractExtension(String fileName) {
		int i = fileName.lastIndexOf(".");
		
		return fileName.substring(i + 1);
	}
	
	@GetMapping
	public ResponseEntity<List<TaskResponseDTO>> getAllTask() {
		List<TaskEntity> taskList = this.taskService.findAllTask();
		
		return ResponseEntity.status(HttpStatus.OK).body(TaskResponseDTO.convertToDTO(taskList));
	}

	@GetMapping("/{id}")
	public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) throws NotFoundException {
		TaskEntity taskEntity = this.taskService.findTaskById(id);
		
		return ResponseEntity.status(HttpStatus.OK).body(TaskResponseDTO.convertToDTO(taskEntity));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @RequestBody TaskRequestDTO taskRequestDTO) throws ParseException, NotFoundException {
		TaskEntity taskEntity = this.taskService.updateTask(id, taskRequestDTO);
		
		return ResponseEntity.status(HttpStatus.OK).body(TaskResponseDTO.convertToDTO(taskEntity));
	}
	@PostMapping("/{id}/complete")
	public ResponseEntity<TaskResponseDTO> completeTask(@PathVariable Long id) throws ParseException, NotFoundException {
		TaskRequestDTO taskRequestDTO = new TaskRequestDTO();
		taskRequestDTO.setSituation(SituationEnum.COMPLETED);
		
		TaskEntity taskEntity = this.taskService.updateTask(id, taskRequestDTO);
		
		return ResponseEntity.status(HttpStatus.OK).body(TaskResponseDTO.convertToDTO(taskEntity));
	}
}
