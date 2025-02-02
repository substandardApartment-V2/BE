package com.myapt.domain.apt.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notices {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notices_id")
	private Long id;

	@Column(nullable = false)
	private String title; // 제목

	@Column(nullable = false)
	private String content; // 내용

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	public LocalDateTime getCreateAt() {
		return createdAt;
	}
}
