package com.mobilefactory.lotto.event.model.service;

import com.mobilefactory.lotto.event.model.dto.CheckResultRequest;
import com.mobilefactory.lotto.event.model.dto.CheckResultResponse;

public interface ResultService {
    CheckResultResponse checkResult(CheckResultRequest request);
}