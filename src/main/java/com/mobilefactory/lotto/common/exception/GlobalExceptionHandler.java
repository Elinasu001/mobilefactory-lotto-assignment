package com.mobilefactory.lotto.common.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mobilefactory.lotto.common.ResponseData;
import com.mobilefactory.lotto.common.exception.auth.AuthCodeExpiredException;
import com.mobilefactory.lotto.common.exception.auth.AuthRequiredException;
import com.mobilefactory.lotto.common.exception.auth.InvalidAuthCodeException;
import com.mobilefactory.lotto.common.exception.event.AlreadyParticipatedException;
import com.mobilefactory.lotto.common.exception.event.EventNotFoundException;
import com.mobilefactory.lotto.common.exception.event.EventNotStartedException;
import com.mobilefactory.lotto.common.exception.event.ExceedMaxParticipantsException;
import com.mobilefactory.lotto.common.exception.result.AnnounceNotStartedException;
import com.mobilefactory.lotto.common.exception.result.ParticipantNotFoundException;
import com.mobilefactory.lotto.common.exception.sms.SmsSendFailedException;
import com.mobilefactory.lotto.common.exception.winner.ForcedWinnerNotFoundException;
import com.mobilefactory.lotto.common.exception.winner.NotEnoughCandidatesException;
import com.mobilefactory.lotto.common.exception.winner.WinnerAlreadyGeneratedException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /* ===================== 인증 ===================== */
    @ExceptionHandler(InvalidAuthCodeException.class)
    public ResponseEntity<ResponseData<Object>> handleInvalidAuthCode(InvalidAuthCodeException e) {
        log.warn("인증번호 불일치: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthCodeExpiredException.class)
    public ResponseEntity<ResponseData<Object>> handleAuthCodeExpired(AuthCodeExpiredException e) {
        log.warn("인증번호 만료: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthRequiredException.class)
    public ResponseEntity<ResponseData<Object>> handleAuthRequired(AuthRequiredException e) {
        log.warn("인증 필요: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /* ===================== 이벤트 참여 ===================== */
    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ResponseData<Object>> handleEventNotFound(EventNotFoundException e) {
        log.warn("이벤트 없음: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EventNotStartedException.class)
    public ResponseEntity<ResponseData<Object>> handleEventNotStarted(EventNotStartedException e) {
        log.warn("이벤트 기간 아님: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyParticipatedException.class)
    public ResponseEntity<ResponseData<Object>> handleAlreadyParticipated(AlreadyParticipatedException e) {
        log.warn("중복 참여: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExceedMaxParticipantsException.class)
    public ResponseEntity<ResponseData<Object>> handleExceedMaxParticipants(ExceedMaxParticipantsException e) {
        log.warn("참여 정원 초과: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /* ===================== 결과 조회 ===================== */
    @ExceptionHandler(ParticipantNotFoundException.class)
    public ResponseEntity<ResponseData<Object>> handleParticipantNotFound(ParticipantNotFoundException e) {
        log.warn("참가자 없음: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AnnounceNotStartedException.class)
    public ResponseEntity<ResponseData<Object>> handleAnnounceNotStarted(AnnounceNotStartedException e) {
        log.warn("발표 기간 아님: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /* ===================== 당첨자 생성 ===================== */
    @ExceptionHandler(ForcedWinnerNotFoundException.class)
    public ResponseEntity<ResponseData<Object>> handleForcedWinnerNotFound(ForcedWinnerNotFoundException e) {
        log.warn("1등 지정 번호 미참여: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WinnerAlreadyGeneratedException.class)
    public ResponseEntity<ResponseData<Object>> handleWinnerAlreadyGenerated(WinnerAlreadyGeneratedException e) {
        log.warn("당첨자 이미 생성됨: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotEnoughCandidatesException.class)
    public ResponseEntity<ResponseData<Object>> handleNotEnoughCandidates(NotEnoughCandidatesException e) {
        log.warn("대상자 부족: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /* ===================== 문자 발송 ===================== */
    @ExceptionHandler(SmsSendFailedException.class)
    public ResponseEntity<ResponseData<Object>> handleSmsSendFailed(SmsSendFailedException e) {
        log.error("문자 발송 실패: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* ===================== Validation ===================== */
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ResponseData<Object>> handleValidationException(
//            MethodArgumentNotValidException e) {
//
//        Map<String, String> errors = new HashMap<>();
//        e.getBindingResult().getAllErrors().forEach(error -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//
//        log.warn("입력값 검증 실패: {}", errors);
//        return ResponseData.failure(
//                "입력값이 올바르지 않습니다: " + errors,
//                HttpStatus.BAD_REQUEST
//        );
//    }

    /* ===================== 공통 예외 ===================== */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseData<Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("잘못된 인자: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ResponseData<Object>> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException e) {
        log.warn("지원하지 않는 미디어 타입: {}", e.getMessage());
        return ResponseData.failure("지원하지 않는 미디어 타입입니다.", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseData<Object>> handleRuntimeException(RuntimeException e) {
        log.error("RuntimeException 발생: {}", e.getMessage(), e);
        return ResponseData.failure("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseData<Object>> handleException(Exception e) {
        log.error("예상치 못한 오류 발생: {}", e.getMessage(), e);
        return ResponseData.failure("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}