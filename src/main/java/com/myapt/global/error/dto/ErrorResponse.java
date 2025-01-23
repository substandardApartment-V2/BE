package com.myapt.global.error.dto;

public record ErrorResponse(
	int code,
	String message
) {
}