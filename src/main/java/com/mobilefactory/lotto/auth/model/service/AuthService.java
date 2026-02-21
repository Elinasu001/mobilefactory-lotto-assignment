package com.mobilefactory.lotto.auth.model.service;

import com.mobilefactory.lotto.auth.model.dto.AuthResponse;
import com.mobilefactory.lotto.auth.model.dto.SendAuthCodeRequest;
import com.mobilefactory.lotto.auth.model.dto.VerifyAuthCodeRequest;

public interface AuthService {
	// 인증번호 발송 (참여용)
    AuthResponse sendAuthCodeForParticipate(SendAuthCodeRequest request);
    // 인증번호 발송 (결과 조회용)
    AuthResponse sendAuthCodeForResult(SendAuthCodeRequest request);
    // 인증번호 검증
    AuthResponse verifyAuthCode(VerifyAuthCodeRequest request);
}
