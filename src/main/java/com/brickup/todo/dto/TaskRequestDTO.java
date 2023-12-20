package com.brickup.todo.dto;

import java.text.ParseException;

import com.brickup.todo.entity.TaskEntity;
import com.brickup.todo.enums.SituationEnum;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskRequestDTO {	
	
	@NotNull(message = "Description must be defined")
	@NotEmpty(message = "Description cannot be empty")
	private String description;
	
	@NotNull(message = "Situation must be defined")
	@NotEmpty(message = "Situation cannot be empty")
	private SituationEnum situation;

	public TaskEntity convertToEntity() throws ParseException {
		ObjectMapper objMapper = new ObjectMapper();
		objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		TaskEntity taskEntity = objMapper.convertValue(this, TaskEntity.class);

		return taskEntity;
	}

}
