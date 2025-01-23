package com.myapt.global.error.exception;

public abstract class ConflictGroupException extends RuntimeException {
	public ConflictGroupException(String message) {
		super(message);
	}
}
