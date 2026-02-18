package com.mobilefactory.lotto.auth.model.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.mobilefactory.lotto.auth.model.dao.AuthMapper;
import com.mobilefactory.lotto.auth.model.dto.AuthResponse;
import com.mobilefactory.lotto.auth.model.dto.SendAuthCodeRequest;
import com.mobilefactory.lotto.auth.model.vo.PhoneAuth;
import com.mobilefactory.lotto.common.exception.event.AlreadyParticipatedException;
import com.mobilefactory.lotto.common.exception.event.EventNotFoundException;
import com.mobilefactory.lotto.event.model.dao.EventMapper;
import com.mobilefactory.lotto.event.model.dao.ParticipantMapper;
import com.mobilefactory.lotto.event.model.vo.Event;
import com.mobilefactory.lotto.event.model.vo.ParticipantSearchVo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthMapper authMapper;
    private final EventMapper eventMapper;
    private final ParticipantMapper participantMapper;

    @Override
    public AuthResponse sendAuthCode(SendAuthCodeRequest request){

        //log.info("인증번호 발송 시작");
        //log.info("요청 전화번호: {}", request.getPhoneNumber());

        // 1. 현재 진행중인 이벤트 조회
        Event activeEvent = eventMapper.selectActiveEvent();
        if(activeEvent == null){
            throw new EventNotFoundException("진행중인 이벤트가 없습니다.");
        }
        //log.info("현재 진행중인 이벤트: {} (ID: {})", activeEvent.getEventName(), activeEvent.getEventId());

        // 2. 중복 참여 확인
        boolean alreadyParticipated = participantMapper.existsByEventAndPhone(
            ParticipantSearchVo.builder()
                .eventId(activeEvent.getEventId())
                .phoneNumber(request.getPhoneNumber())
                .build()
        );
        if(alreadyParticipated){
            throw new AlreadyParticipatedException("이미 참여한 번호입니다.");
        }
        log.info("중복 참여 여부 결과: {}", alreadyParticipated);

        // 3. 인증번호 생성
        String authCode = generateAuthCode();
        //log.info("생성된 인증번호: {}", authCode);

        // 4. 만료 시간 (3분 후)
        Date expiredAt = getExpiredAt(3);
        //log.info("인증번호 만료 시간: {}", expiredAt);

        // 5. DB 저장
        PhoneAuth phoneAuth = PhoneAuth.builder()
            .phoneNumber(request.getPhoneNumber())
            .authCode(authCode)
            .isVerified("N")
            .expiredAt(expiredAt)
            .build();

        log.info("저장할 PhoneAuth 객체: {}", phoneAuth);

        int insertResult = authMapper.insertPhoneAuth(phoneAuth);
        log.info("인증 정보 저장 결과: {}", insertResult);
        log.info("저장 후 authId: {}", phoneAuth.getAuthId());

        if(insertResult != 1){
            throw new RuntimeException("인증 정보 저장에 실패했습니다.");
        }
        log.info(" DB 저장 성공");

        // 6. Mock SMS
        sendMockSms(request.getPhoneNumber(), authCode);

        // 7. 응답 생성
        AuthResponse response = AuthResponse.builder()
            .authId(phoneAuth.getAuthId())
            .isVerified(false)
            .expiredAt(expiredAt)
            .build();

        log.info("응답 데이터: {}", response);

        return response;
    }

    @Override
    public AuthResponse verifyAuthCode(String phoneNumber, String authCode) {
        return null;
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
