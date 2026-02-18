package com.mobilefactory.lotto.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mobilefactory.lotto.auth.model.dto.AuthResponse;
import com.mobilefactory.lotto.auth.model.dto.SendAuthCodeRequest;
import com.mobilefactory.lotto.auth.model.service.AuthService;
import com.mobilefactory.lotto.common.ResponseData;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 인증번호 발송
     */
    @PostMapping("/send")
    public ResponseEntity<ResponseData<AuthResponse>> sendAuthCode(
            @Valid @RequestBody SendAuthCodeRequest request) {

        AuthResponse response = authService.sendAuthCode(request);

        return ResponseData.ok(response, "인증번호가 발송되었습니다.");
    }

    /**
     * 인증번호 검증
     */
    @PostMapping("/verify")
    public ResponseEntity<ResponseData<AuthResponse>> verifyAuthCode(
            @Valid @RequestBody SendAuthCodeRequest request) {

        AuthResponse response = authService.verifyAuthCode(
            request.getPhoneNumber(),
            request.getAuthCode()
        );

        return ResponseData.ok(response, "인증이 완료되었습니다.");
    }
}