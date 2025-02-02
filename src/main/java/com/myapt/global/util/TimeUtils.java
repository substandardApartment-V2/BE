package com.myapt.global.util;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeUtils {
	public static String formatTimeDifference(LocalDateTime dateTime) {
		LocalDateTime now = LocalDateTime.now();
		Duration duration = Duration.between(dateTime, now);

		if (duration.toMinutes() < 1) {
			return "방금 전";
		} else if (duration.toMinutes() < 60) {
			return duration.toMinutes() + "분 전";
		} else if (duration.toHours() < 24) {
			return duration.toHours() + "시간 전";
		} else if (duration.toDays() < 7) {
			return duration.toDays() + "일 전";
		} else if (duration.toDays() < 30) {
			return (duration.toDays() / 7) + "주 전";
		} else if (duration.toDays() < 365) {
			return (duration.toDays() / 30) + "달 전";
		} else {
			return (duration.toDays() / 365) + "년 전";
		}
	}
}