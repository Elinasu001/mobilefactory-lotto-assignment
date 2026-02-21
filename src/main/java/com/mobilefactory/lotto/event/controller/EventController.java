package com.mobilefactory.lotto.event.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mobilefactory.lotto.common.ResponseData;
import com.mobilefactory.lotto.event.model.dto.CheckResultRequest;
import com.mobilefactory.lotto.event.model.dto.CheckResultResponse;
import com.mobilefactory.lotto.event.model.dto.EventPublicResponse;
import com.mobilefactory.lotto.event.model.dto.ParticipateRequest;
import com.mobilefactory.lotto.event.model.dto.ParticipateResponse;
import com.mobilefactory.lotto.event.model.service.EventService;
import com.mobilefactory.lotto.event.model.service.ResultService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final ResultService resultService;

    /**
     * 이벤트 참여
     */
    @PostMapping("/participate")
    public ResponseEntity<ResponseData<ParticipateResponse>> participate(
            @Valid @RequestBody ParticipateRequest request) {
        ParticipateResponse response = eventService.participate(request);
        return ResponseData.created(response, "이벤트 참여가 완료되었습니다.");
    }

    /**
     * 진행중인 이벤트 조회
     */
    @GetMapping("/{eventId}")
    public ResponseEntity<ResponseData<EventPublicResponse>> getPublicActiveEvent(
        @PathVariable("eventId") Long eventId) {
        EventPublicResponse activeEvent = eventService.getPublicActiveEvent(eventId);
        return ResponseData.ok(activeEvent, "현재 진행중인 이벤트입니다.");
    }

    /**
     * 결과 조회
     */
    @PostMapping("/result")
    public ResponseEntity<ResponseData<CheckResultResponse>> checkResult(
            @Valid @RequestBody CheckResultRequest request) {
        CheckResultResponse response = resultService.checkResult(request);
        return ResponseData.ok(response, "당첨 결과 조회가 완료되었습니다.");
    }


}
