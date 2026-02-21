package com.mobilefactory.lotto.event.model.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mobilefactory.lotto.auth.model.dao.AuthMapper;
import com.mobilefactory.lotto.auth.model.vo.PhoneAuth;
import com.mobilefactory.lotto.auth.model.vo.PhoneAuthSearchVo;
import com.mobilefactory.lotto.common.exception.auth.AuthRequiredException;
import com.mobilefactory.lotto.common.exception.result.ParticipantNotFoundException;
import com.mobilefactory.lotto.event.model.dao.ParticipantMapper;
import com.mobilefactory.lotto.event.model.dao.ResultCheckMapper;
import com.mobilefactory.lotto.event.model.dto.CheckResultRequest;
import com.mobilefactory.lotto.event.model.dto.CheckResultResponse;
import com.mobilefactory.lotto.event.model.vo.Event;
import com.mobilefactory.lotto.event.model.vo.Participant;
import com.mobilefactory.lotto.event.model.vo.ParticipantSearchVo;
import com.mobilefactory.lotto.event.model.vo.ResultCheck;
import com.mobilefactory.lotto.event.util.EventValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final EventValidator eventValidator;
    private final ParticipantMapper participantMapper;
    private final ResultCheckMapper resultCheckMapper;
    private final AuthMapper authMapper;

    @Override
    @Transactional
    public CheckResultResponse checkResult(CheckResultRequest request) {

        Long eventId = request.getEventId();
        String phoneNumber = request.getPhoneNumber();
        String authCode = request.getAuthCode();

        // 1. 인증 확인
        PhoneAuth auth = authMapper.selectByPhoneAndCode(
            PhoneAuthSearchVo.builder()
                .phoneNumber(phoneNumber)
                .authCode(authCode)
                .build()
        );

        if (auth == null) {
            throw new AuthRequiredException("본인 인증이 필요합니다.");
        }

        // 2. 발표된 이벤트 조회
        Event event = eventValidator.getAnnouncedEvent(eventId);
        //log.info("당첨 결과 조회 - 이벤트 ID: {}, 이벤트명: {}", event.getEventId(), event.getEventName());


        // 3. 발표 기간 확인
        Date now = new Date();
        if(now.before(event.getAnnounceStart()) || now.after(event.getAnnounceEnd())){
            throw new IllegalStateException("발표 기간이 아닙니다.");
        }

        // 4. 참가자 조회
        Participant participant = participantMapper.selectByPhone(
            ParticipantSearchVo.builder()
                .eventId(event.getEventId())
                .phoneNumber(request.getPhoneNumber())
                .build()
        );
        if(participant == null){
            throw new ParticipantNotFoundException("참가자 정보를 찾을 수 없습니다.");
        }

        // 5. 기존 확인 이력 조회
        ResultCheck existingCheck = resultCheckMapper.selectByParticipantId(
            participant.getParticipantId()
        );

        //log.info("기존 확인 이력 조회 : {} (참가자 ID: {})", existingCheck, participant.getParticipantId());

        int checkCount;
        if (existingCheck == null) {
            // 최초 확인
            checkCount = 1;
            resultCheckMapper.insertResultCheck(
                ResultCheck.builder()
                    .participantId(participant.getParticipantId())
                    .checkCount(checkCount)
                    .build()
            );
            //log.info("최초 확인 - checkCount: {}", checkCount);
        } else {
            // 두 번째 이후 확인
            checkCount = existingCheck.getCheckCount() + 1;
            if (checkCount <= 2) {
                resultCheckMapper.updateCheckCount(
                    ResultCheck.builder()
                    .participantId(participant.getParticipantId())
                    .checkCount(checkCount)
                    .build()
                );
                //log.info("{}번째 확인 - checkCount 업데이트", checkCount);
            } else {
                checkCount = existingCheck.getCheckCount(); // 2로 유지
                //log.info("3번째 이상 확인 - checkCount 유지: {}", checkCount);
            }
        }

        log.info("최종 확인 횟수: {}", checkCount);

        // 6. 응답 생성
        return buildResponse(event, participant, checkCount);
    }


    /**
     * 응답 생성
     */
    private CheckResultResponse buildResponse(Event event, Participant participant, int checkCount) {

        boolean isWinner = "Y".equals(participant.getIsWinner());
        Integer prizeRank = participant.getPrizeRank();

        // 메시지 생성
        String message;
        Integer displayRank = null;  // 표시할 등수

        if (checkCount == 1) {
            // 1차 조회: 등수 공개
            displayRank = prizeRank;

            if (isWinner) {
                message = String.format("축하합니다! %d등에 당첨되셨습니다!", prizeRank);
            } else {
                message = "아쉽게도 당첨되지 않으셨습니다.";
            }
        } else {
            // 2차 조회: 등수 숨김, 당첨 여부만
            displayRank = null;  // 등수 숨김!

            if (isWinner) {
                message = "당첨되셨습니다.";
            } else {
                message = "당첨되지 않으셨습니다.";
            }
        }

        return CheckResultResponse.builder()
            .eventId(event.getEventId())
            .eventName(event.getEventName())
            .phoneNumber(participant.getPhoneNumber())
            .isWinner(isWinner)
            .prizeRank(displayRank)  // 1차: 등수, 2차: null
            .checkCount(checkCount)
            .checkMessage(message)
            .build();
    }


}