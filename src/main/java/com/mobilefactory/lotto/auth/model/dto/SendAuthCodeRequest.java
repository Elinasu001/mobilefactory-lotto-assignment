package com.mobilefactory.lotto.auth.model.dto;

import org.hibernate.validator.constraints.NotBlank;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SendAuthCodeRequest {

    @NotBlank(message = "휴대폰 번호는 필수입니다.")
    @Pattern(
        regexp = "^01[0-9]{8,9}$",
        message = "올바른 휴대폰 번호 형식이 아닙니다."
    )
    private String phoneNumber;

    // 인증번호 검증 시 사용 (발송 시에는 null)
    private String authCode;
}
