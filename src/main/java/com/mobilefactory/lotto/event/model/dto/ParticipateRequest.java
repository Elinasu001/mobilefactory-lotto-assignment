package com.mobilefactory.lotto.event.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ParticipateRequest {

    private Long eventId;

    @NotBlank(message = "휴대폰 번호는 필수입니다.")
    @Pattern(regexp = "^01[0-9]{8,9}$", message = "올바른 휴대폰 번호 형식이 아닙니다.")
    private String phoneNumber;

    @NotBlank(message = "인증번호는 필수입니다.")
    @Pattern(regexp = "^[0-9]{6}$", message = "인증번호는 6자리 숫자입니다.")
    private String authCode;
}
