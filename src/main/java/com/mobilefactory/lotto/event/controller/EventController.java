package com.mobilefactory.lotto.event.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mobilefactory.lotto.common.ResponseData;
import com.mobilefactory.lotto.event.model.dto.ParticipateRequest;
import com.mobilefactory.lotto.event.model.dto.ParticipateResponse;
import com.mobilefactory.lotto.event.model.service.EventService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    /**
     * 이벤트 참여
     */
    @PostMapping("/participate")
    public ResponseEntity<ResponseData<ParticipateResponse>> participate(
            @Valid @RequestBody ParticipateRequest request) {

        ParticipateResponse response = eventService.participate(request);

        return ResponseData.created(response, "이벤트 참여가 완료되었습니다.");
    }
}
