package com.mobilefactory.lotto.admin.event.model.service;

import com.mobilefactory.lotto.admin.event.model.dto.ForceWinnerGenerationRequest;
import com.mobilefactory.lotto.event.model.vo.Event;

public interface AdminEventService {
    public Event forceGenerateWinner(ForceWinnerGenerationRequest request);
}
