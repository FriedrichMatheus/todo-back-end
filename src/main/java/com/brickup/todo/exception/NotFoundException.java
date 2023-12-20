package com.brickup.todo.exception;

public class NotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public NotFoundException(String message) {
		super(message);
	}
}
