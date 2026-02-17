package com.mobilefactory.lotto.common;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseData<T> {
	private String message;             //응답 메시지
	private T data;                     //실제 응답 데이터
	private boolean success;            //성공 여부
	private LocalDateTime timestamp;    //응답 생성 시각

	private ResponseData(String message, T data, boolean success, LocalDateTime timestamp) {
		this.message = message;
		this.data = data;
		this.success = success;
		this.timestamp = timestamp;

	}

	// 200 OK - 성공 응답 (메시지 없이 데이터만 반환)
	public static <T> ResponseEntity<ResponseData<T>> ok(T data) {
		return ResponseEntity.ok(new ResponseData<T>(null, data, true, LocalDateTime.now()));

	}

    // 200 OK - 성공 응답 (데이터 + 메시지 포함)
	public static <T> ResponseEntity<ResponseData<T>> ok(T data, String message) {
		return ResponseEntity.ok(new ResponseData<T>(message, data, true, LocalDateTime.now()));
	}

	// 실패 응답 (HTTP 상태코드와 함께 반환) (예: 400, 401, 403, 404, 500 등)
	public static <T> ResponseEntity<ResponseData<T>> failure(String message, HttpStatus status) {
		return ResponseEntity.status(status).body(new ResponseData<T>(message, null, false, LocalDateTime.now()));
	}

	// 204 No Content - 본문 없이 상태 코드만 반환
	public static <T> ResponseEntity<ResponseData<T>> noContent() {
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	// 201 Created - 리소스 생성 성공 응답
	public static <T> ResponseEntity<ResponseData<T>> created(T data, String message) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseData<>(message, data, true, LocalDateTime.now()));
	}

}