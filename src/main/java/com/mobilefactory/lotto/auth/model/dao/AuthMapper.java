package com.mobilefactory.lotto.auth.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mobilefactory.lotto.auth.model.vo.PhoneAuth;
import com.mobilefactory.lotto.auth.model.vo.PhoneAuthSearchVo;

@Mapper
public interface AuthMapper {

    // 인증번호 저장
    int insertPhoneAuth(PhoneAuth phoneAuth);

    // 전화번호 + 인증번호로 조회 (단일 VO로)
    PhoneAuth selectByPhoneAndCode(PhoneAuthSearchVo searchVo);

    // 인증 완료 처리
    int updateVerified(Long authId);
}
