package com.mobilefactory.lotto.auth.model.service;

import com.mobilefactory.lotto.auth.model.dto.AuthResponse;
import com.mobilefactory.lotto.auth.model.dto.SendAuthCodeRequest;

public interface AuthService {
	// 인증번호 발송
    AuthResponse sendAuthCode(SendAuthCodeRequest request);
    // 인증번호 검증
    AuthResponse verifyAuthCode(SendAuthCodeRequest request);
}
