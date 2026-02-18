package com.mobilefactory.lotto.event.model.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mobilefactory.lotto.auth.model.dao.AuthMapper;
import com.mobilefactory.lotto.auth.model.vo.PhoneAuth;
import com.mobilefactory.lotto.auth.model.vo.PhoneAuthSearchVo;
import com.mobilefactory.lotto.common.exception.auth.AuthRequiredException;
import com.mobilefactory.lotto.common.exception.event.AlreadyParticipatedException;
import com.mobilefactory.lotto.common.exception.event.EventNotFoundException;
import com.mobilefactory.lotto.common.exception.event.ExceedMaxParticipantsException;
import com.mobilefactory.lotto.event.model.dao.EventMapper;
import com.mobilefactory.lotto.event.model.dao.ParticipantMapper;
import com.mobilefactory.lotto.event.model.dto.ParticipateRequest;
import com.mobilefactory.lotto.event.model.dto.ParticipateResponse;
import com.mobilefactory.lotto.event.model.vo.Event;
import com.mobilefactory.lotto.event.model.vo.Participant;
import com.mobilefactory.lotto.event.model.vo.ParticipantSearchVo;
import com.mobilefactory.lotto.sms.model.dao.SmsLogMapper;
import com.mobilefactory.lotto.sms.model.vo.SmsLog;
import com.mobilefactory.lotto.util.LottoNumberGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventMapper eventMapper;
    private final ParticipantMapper participantMapper;
    private final AuthMapper authMapper;
    private final SmsLogMapper smsLogMapper;

    @Override
    @Transactional
    public ParticipateResponse participate(ParticipateRequest request) {

        //log.info("== 참여 처리 시작 ==");

        String phoneNumber = request.getPhoneNumber();
        String authCode = request.getAuthCode();

        // 1. 인증 완료 여부 확인
        PhoneAuth phoneAuth = authMapper.selectVerifiedByPhoneAndCode(
            PhoneAuthSearchVo.builder()
                .phoneNumber(phoneNumber)
                .authCode(authCode)
                .build()
        );

        if(phoneAuth == null || !"Y".equals(phoneAuth.getIsVerified())){
            throw new AuthRequiredException("휴대폰 인증이 필요합니다.");
        }

        //log.info("휴대폰 인증 완료: {}", phoneAuth);

        // 2. 현재 진행중인 이벤트 조회
        Event activeEvent = eventMapper.selectActiveEvent();
        if(activeEvent == null){
            throw new EventNotFoundException("진행중인 이벤트가 없습니다.");
        }
        //log.info("현재 진행중인 이벤트: {} (ID: {})", activeEvent.getEventName(), activeEvent.getEventId());

        Long eventId = activeEvent.getEventId();

        // 3. 중복 참여 재확인
        boolean alreadyParticipated = participantMapper.existsByEventAndPhone(
            ParticipantSearchVo.builder()
                .eventId(eventId)
                .phoneNumber(phoneNumber)
                .build()
        );

        if(alreadyParticipated){
            throw new AlreadyParticipatedException("이미 참여한 번호입니다.");
        }
        //log.info("중복 참여 여부 결과: {}", alreadyParticipated);

        // 4. 다음 참가자 순번 조회
        Integer participantNo = participantMapper.getNextParticipantNo(eventId);
        //log.info("다음 참가자 순번: {}", participantNo);

        // 5. 정원 초과 확인
        if (participantNo > activeEvent.getMaxParticipants()) {
            throw new ExceedMaxParticipantsException("참여 정원이 마감되었습니다.");
        }

        // 6. 로또 번호 생성
        String lottoNumbers = LottoNumberGenerator.generate();
        //log.info("생성된 로또 번호: {}", lottoNumbers);


        // 7. 참가자 등록
        Participant participant = Participant.builder()
            .eventId(eventId)
            .phoneNumber(phoneNumber)
            .participantNo(participantNo)
            .lottoNumbers(lottoNumbers)
            .isWinner("N")
            .build();
        //log.info("저장할 Participant 객체: {}", participant);

        int insertResult = participantMapper.insertParticipant(participant);
        if(insertResult != 1){
            throw new RuntimeException("참가자 등록 실패");
        }
        //log.info("참가자 정보 저장 결과: {}", insertResult);

        // 8. 참여 완료 문자 발송 (Mock)
        sendParticipationSms(participant);

        ParticipateResponse participateResponse = ParticipateResponse.builder()
            .participantId(participant.getParticipantId())
            .participantNo(participantNo)
            .lottoNumbers(lottoNumbers)
            .participatedAt(participant.getParticipatedAt())
            .build();

        //log.info("참여 응답 데이터: {}", participateResponse);

        return participateResponse;
    }


    // Mock SMS 발송 + DB 저장
    private void sendParticipationSms(Participant participant) {
        String message = String.format(
            "[모바일팩토리] 로또 이벤트 참여 완료!\n" +
            "참가번호: %d번\n" +
            "로또번호: %s\n" +
            "발표: 2026.04.01~04.15",
            participant.getParticipantNo(),
            participant.getLottoNumbers().replace(",", " ")
        );

        log.info("========================================");
        log.info("[Mock SMS] 참여 완료 문자 발송");
        log.info("수신번호: {}", participant.getPhoneNumber());
        log.info("메시지: {}", message);
        log.info("========================================");

        // DB 이력 저장
        SmsLog smsLog = SmsLog.builder()
            .participantId(participant.getParticipantId())
            .smsType("PARTICIPATION")
            .messageContent(message)
            .sendStatus("SUCCESS")
            .build();

        smsLogMapper.insertSmsLog(smsLog);
    }
}
