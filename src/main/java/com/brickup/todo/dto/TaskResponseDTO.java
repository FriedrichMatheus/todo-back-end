package com.brickup.todo.dto;

import java.util.List;

import com.brickup.todo.entity.TaskEntity;
import com.brickup.todo.enums.SituationEnum;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
public class TaskResponseDTO {

	private Long id;
	private String description;
	private String imagePath;
	private SituationEnum situation;

	public static TaskResponseDTO convertToDTO(TaskEntity taskEntity) {
		ObjectMapper objMapper = new ObjectMapper();
		objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		TaskResponseDTO taskResponseDTO = objMapper.convertValue(taskEntity, TaskResponseDTO.class); 

		return taskResponseDTO;
	}

	public static List<TaskResponseDTO> convertToDTO(List<TaskEntity> taskList) {
		return taskList.stream().map(taskEntity -> TaskResponseDTO.convertToDTO(taskEntity)).toList();
	}

}
