package com.mobilefactory.lotto.admin.event.model.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mobilefactory.lotto.admin.event.model.dao.AdminEventMapper;
import com.mobilefactory.lotto.admin.event.model.dto.ForceWinnerGenerationRequest;
import com.mobilefactory.lotto.common.exception.event.EventNotFoundException;
import com.mobilefactory.lotto.event.model.dao.EventMapper;
import com.mobilefactory.lotto.event.model.vo.Event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService{
    private final AdminEventMapper adminEventMapper;
    private final EventMapper eventMapper;

    @Override
    @Transactional
    public Event forceGenerateWinner(ForceWinnerGenerationRequest request) {

        Long eventId = request.getEventId();
        String forcedWinnerPhone = request.getForcedWinnerPhone();
        //log.info("당첨자 강제 생성 시작");
        //log.info("이멘트 ID: {}", eventId);
        //log.info("1등 지정 번호: {}", forcedWinnerPhone);

        // 1. 이벤트 존재 확인
        Event activeEvent = eventMapper.selectActiveEvent();
        if(activeEvent == null){
            throw new EventNotFoundException("진행중인 이벤트가 없습니다.");
        }
        //log.info("현재 진행중인 이벤트 : {} (ID: {})", activeEvent.getEventName(), activeEvent.getEventId());

        // 2. 1등 지정 번호 업데이트
        int updated = adminEventMapper.updateForceWinnerPhone(
            ForceWinnerGenerationRequest.builder()
                .eventId(eventId)
                .forcedWinnerPhone(forcedWinnerPhone)
                .build()
        );

        if(updated == 0){
            throw new RuntimeException("1등 지정 번호 업데이트에 실패하였습니다.");
        }

        //log.info("1등 지정 번호 설정 완료");

        return eventMapper.selectById(eventId);
    }
}
