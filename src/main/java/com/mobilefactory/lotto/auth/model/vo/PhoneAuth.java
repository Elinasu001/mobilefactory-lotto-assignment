package com.mobilefactory.lotto.auth.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneAuth {
    private Long authId;        // 인증Id
    private String phoneNumber; // 휴대폰번호
    private String authCode;    // 인증번호
    private String isVerified;  // 인증완료여부(Y/N)
    private Date expiredAt;     // 인증만료시간
    private Date createdAt;     // 생성일
}