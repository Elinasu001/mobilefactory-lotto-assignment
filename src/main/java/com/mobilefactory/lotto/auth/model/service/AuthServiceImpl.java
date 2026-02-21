package com.mobilefactory.lotto.auth.model.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mobilefactory.lotto.auth.model.dao.AuthMapper;
import com.mobilefactory.lotto.auth.model.dto.AuthResponse;
import com.mobilefactory.lotto.auth.model.dto.SendAuthCodeRequest;
import com.mobilefactory.lotto.auth.model.dto.VerifyAuthCodeRequest;
import com.mobilefactory.lotto.auth.model.vo.PhoneAuth;
import com.mobilefactory.lotto.auth.model.vo.PhoneAuthSearchVo;
import com.mobilefactory.lotto.common.exception.auth.InvalidAuthCodeException;
import com.mobilefactory.lotto.common.exception.event.AlreadyParticipatedException;
import com.mobilefactory.lotto.common.exception.result.ParticipantNotFoundException;
import com.mobilefactory.lotto.event.model.dao.ParticipantMapper;
import com.mobilefactory.lotto.event.model.vo.Event;
import com.mobilefactory.lotto.event.model.vo.ParticipantSearchVo;
import com.mobilefactory.lotto.event.util.EventValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthMapper authMapper;
    private final ParticipantMapper participantMapper;
    private final EventValidator eventValidator;

    /**
     * 참여용 인증번호 발송
     */
    @Override
    @Transactional
    public AuthResponse sendAuthCodeForParticipate(SendAuthCodeRequest request) {

        Long eventId = request.getEventId();
        String phoneNumber = request.getPhoneNumber();

        // 1. ACTIVE 이벤트 조회
        Event event = eventValidator.getActiveEvent(eventId);

        // 2. 중복 참여 확인
        boolean alreadyParticipated = participantMapper.existsByEventAndPhone(
            ParticipantSearchVo.builder()
                .eventId(event.getEventId())
                .phoneNumber(phoneNumber)
                .build()
        );

        if (alreadyParticipated) {
            throw new AlreadyParticipatedException("이미 참여한 번호입니다.");
        }
        //log.info("중복 참여 여부 결과: {}", alreadyParticipated);

        // 3. 인증번호 발송 (공통 로직)
        return sendAuthCode(phoneNumber);
    }

    /**
     * 결과 조회용 인증번호 발송
     */
    @Override
    @Transactional
    public AuthResponse sendAuthCodeForResult(SendAuthCodeRequest request) {

        Long eventId = request.getEventId();
        String phoneNumber = request.getPhoneNumber();

        // 1. ANNOUNCED 이벤트 조회
        Event event = eventValidator.getAnnouncedEvent(eventId);

        // 2. 참여 이력 확인
        boolean participated = participantMapper.existsByEventAndPhone(
            ParticipantSearchVo.builder()
                .eventId(event.getEventId())
                .phoneNumber(phoneNumber)
                .build()
        );
        if (!participated) {
            throw new ParticipantNotFoundException("참여 이력이 없습니다.");
        }
        //log.info("참여 이력 여부 결과: {}", participated);

        // 3. 인증번호 발송 (공통 로직)
        return sendAuthCode(phoneNumber);
    }

    /**
     * 인증번호 발송 공통 로직
     */
    private AuthResponse sendAuthCode(String phoneNumber) {

        // 인증번호 생성
        String authCode = generateAuthCode();

        // 만료 시간 (3분 후)
        Date expiredAt = getExpiredAt(3);

        // DB 저장
        PhoneAuth phoneAuth = PhoneAuth.builder()
            .phoneNumber(phoneNumber)
            .authCode(authCode)
            .isVerified("N")
            .expiredAt(expiredAt)
            .build();
        //log.info("저장할 PhoneAuth 객체: {}", phoneAuth);

        int insertResult = authMapper.insertPhoneAuth(phoneAuth);
        if (insertResult != 1) {
            throw new RuntimeException("인증 정보 저장에 실패했습니다.");
        }

        // Mock SMS
        sendMockSms(phoneNumber, authCode);

        // 응답 생성
        return AuthResponse.builder()
            .authId(phoneAuth.getAuthId())
            .isVerified(false)
            .expiredAt(expiredAt)
            .build();
    }


    @Override
    @Transactional
    public AuthResponse verifyAuthCode(VerifyAuthCodeRequest request) {

        //log.info("== 인증번호 검증 시작 ==");
        //log.info("전화번호: {}, 인증번호: {}", request.getPhoneNumber(), request.getAuthCode());

        // 1. Builder로 SearchVo 생성 후 조회
        PhoneAuthSearchVo searchVo = PhoneAuthSearchVo.builder()
            .phoneNumber(request.getPhoneNumber())
            .authCode(request.getAuthCode())
            .build();
        //log.info("검색 조건: {}", searchVo);

        PhoneAuth phoneAuth = authMapper.selectVerifiedByPhoneAndCode(searchVo);
        //log.info("조회 결과: {}", phoneAuth);

        // 2. 인증번호 불일치
        if(phoneAuth == null){
            throw new InvalidAuthCodeException("인증번호가 일치하지 않습니다.");
        }

        // 3. 만료 확인
        Date now = new Date();
        //log.info("현재 시간: {}", now);
        //log.info("만료 시간: {}", phoneAuth.getExpiredAt());
        if(now.after(phoneAuth.getExpiredAt())) {
            throw new InvalidAuthCodeException("인증번호가 만료되었습니다.");
        }
        //log.info("== 유효한 인증번호 ==");

        // 4. 인증 완료 처리
        int updateResult = authMapper.updateVerified(phoneAuth.getAuthId());
        //log.info("업데이트 결과: {} (1이면 성공)", updateResult);
        if (updateResult != 1) {
            throw new RuntimeException("인증 완료 처리 실패");
        }
        //log.info("== 인증 완료 처리 성공 ==");

        // 5. 응답
        AuthResponse response = AuthResponse.builder()
            .authId(phoneAuth.getAuthId())
            .isVerified(true)
            .expiredAt(phoneAuth.getExpiredAt())
            .build();
        //log.info("응답 데이터: {}", response);

        return response;
    }


    // 6자리 랜덤 숫자
    private String generateAuthCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    // 만료 시간 계산
    private Date getExpiredAt(int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    // Mock SMS
    private void sendMockSms(String phoneNumber, String authCode) {
        log.info("========================================");
        log.info("[Mock SMS] 인증번호 발송");
        log.info("수신번호: {}", phoneNumber);
        log.info("인증번호: {}", authCode);
        log.info("내용: [모바일팩토리] 인증번호 [{}] 3분 내 입력해주세요.", authCode);
        log.info("========================================");
    }

}
