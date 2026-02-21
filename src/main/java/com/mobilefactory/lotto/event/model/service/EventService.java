package com.mobilefactory.lotto.event.model.service;

import com.mobilefactory.lotto.event.model.dto.EventPublicResponse;
import com.mobilefactory.lotto.event.model.dto.ParticipateRequest;
import com.mobilefactory.lotto.event.model.dto.ParticipateResponse;

public interface EventService {
    /**
    * 이벤트 참여
    */
    ParticipateResponse participate(ParticipateRequest request);

    /**
     * 진행중인 이벤트 조회 - 민감 정보 제외
     */
    EventPublicResponse getPublicActiveEvent(Long eventId);

}
