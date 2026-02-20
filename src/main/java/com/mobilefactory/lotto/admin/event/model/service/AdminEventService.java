package com.mobilefactory.lotto.admin.event.model.service;

import com.mobilefactory.lotto.admin.event.model.dto.CreateEventRequest;
import com.mobilefactory.lotto.admin.event.model.dto.ForceWinnerGenerationRequest;
import com.mobilefactory.lotto.event.model.vo.Event;

public interface AdminEventService {
    // 이벤트 생성
    Event createEvent(CreateEventRequest request);
    // 1등 지정 번호 설정
    public Event forceGenerateWinner(ForceWinnerGenerationRequest request);
}
