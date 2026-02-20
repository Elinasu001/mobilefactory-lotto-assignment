package com.mobilefactory.lotto.event.model.service;

import com.mobilefactory.lotto.event.model.dto.ParticipateRequest;
import com.mobilefactory.lotto.event.model.dto.ParticipateResponse;
import com.mobilefactory.lotto.event.model.vo.Event;

public interface EventService {
    ParticipateResponse participate(ParticipateRequest request);

    /**
     * 진행중인 이벤트 조회
     */
    Event getActiveEvent(Long eventId);
}
