package com.mobilefactory.lotto.auth.model.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private Long authId;        // 인증Id
    private Boolean isVerified; // 인증완료여부(Y/N)
    private Date expiredAt;     // 인증만료시간
}