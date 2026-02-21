package com.mobilefactory.lotto.admin.event.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mobilefactory.lotto.admin.event.model.dto.CreateEventRequest;
import com.mobilefactory.lotto.admin.event.model.dto.ForceWinnerGenerationRequest;
import com.mobilefactory.lotto.admin.event.model.service.AdminEventService;
import com.mobilefactory.lotto.common.ResponseData;
import com.mobilefactory.lotto.event.model.vo.Event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final AdminEventService adminEventService;

    /**
     * 이벤트 생성
     */
    @PostMapping("/event")
    public ResponseEntity<ResponseData<Event>> createEvent(
            @Valid @RequestBody CreateEventRequest request) {

        Event event = adminEventService.createEvent(request);

        return ResponseData.created(event, "이벤트가 생성되었습니다.");
    }

    /***
     * 1등 지정 번호 설정
     */
    @PostMapping("/forcewinner")
    public ResponseEntity<ResponseData<Event>> forceGenerateWinner(
    @Valid @RequestBody ForceWinnerGenerationRequest request) {
        Event updateEvent = adminEventService.forceGenerateWinner(request);
        return ResponseData.created(updateEvent, "당첨자 생성이 완료되었습니다.");
    }
}
