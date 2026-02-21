package com.mobilefactory.lotto.admin.event.model.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mobilefactory.lotto.admin.event.model.dao.AdminEventMapper;
import com.mobilefactory.lotto.admin.event.model.dto.CreateEventRequest;
import com.mobilefactory.lotto.admin.event.model.dto.ForceWinnerGenerationRequest;
import com.mobilefactory.lotto.event.model.dao.EventMapper;
import com.mobilefactory.lotto.event.model.vo.Event;
import com.mobilefactory.lotto.event.util.EventValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService{

    private final AdminEventMapper adminEventMapper;
    private final EventMapper eventMapper;
    // private final EventService eventService;
    private final EventValidator eventValidator;

    /**
     * 이벤트 생성
     */
    @Override
    @Transactional
    public Event createEvent(CreateEventRequest request) {

        //log.info("이벤트 생성 시작");
        //log.info("이벤트명: {}", request.getEventName());

        // 1. Event 객체 생성
        Event event = Event.builder()
            .eventName(request.getEventName())
            .startDate(request.getStartDate())
            .endDate(request.getEndDate())
            .announceStart(request.getAnnounceStart())
            .announceEnd(request.getAnnounceEnd())
            .maxParticipants(request.getMaxParticipants())
            .totalWinners(request.getTotalWinners())
            .status("ACTIVE")
            .build();

        // 2. 이벤트 생성
        int inserted = adminEventMapper.insertEvent(event);

        if(inserted == 0){
            throw new RuntimeException("이벤트 생성에 실패하였습니다.");
        }

        //log.info("이벤트 생성 완료 - ID: {}", event.getEventId());

        return event;
    }


    /**
     * 1등 지정 번호 설정
     */
    @Override
    @Transactional
    public Event forceGenerateWinner(ForceWinnerGenerationRequest request) {

        Long eventId = request.getEventId();
        String forcedWinnerPhone = request.getForcedWinnerPhone();
        //log.info("당첨자 강제 생성 시작");
        //log.info("이멘트 ID: {}", eventId);
        //log.info("1등 지정 번호: {}", forcedWinnerPhone);

        // 1. 이벤트 존재 확인
        Event event = eventValidator.getClosedEvent(eventId);
        //log.info("현재 진행중인 이벤트 : {} (ID: {})", activeEvent.getEventName(), activeEvent.getEventId());

        // 2. 1등 지정 번호 업데이트
        int updated = adminEventMapper.updateForceWinnerPhone(
            ForceWinnerGenerationRequest.builder()
                .eventId(event.getEventId())
                .forcedWinnerPhone(forcedWinnerPhone)
                .build()
        );

        if(updated == 0){
            throw new RuntimeException("1등 지정 번호 업데이트에 실패하였습니다.");
        }

        //log.info("1등 지정 번호 설정 완료");

        return eventMapper.selectById(event.getEventId());
    }
}
