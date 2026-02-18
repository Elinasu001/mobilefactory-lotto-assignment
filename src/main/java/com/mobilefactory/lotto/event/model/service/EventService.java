package com.mobilefactory.lotto.event.model.service;

import com.mobilefactory.lotto.event.model.dto.ParticipateRequest;
import com.mobilefactory.lotto.event.model.dto.ParticipateResponse;

public interface EventService {
    ParticipateResponse participate(ParticipateRequest request);
}
