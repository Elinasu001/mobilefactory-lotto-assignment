package com.mobilefactory.lotto.admin.winner.model.service;

import com.mobilefactory.lotto.admin.winner.model.dto.GenerateWinnersRequest;
import com.mobilefactory.lotto.admin.winner.model.dto.WinnerGenerationResponse;

public interface AdminWinnerService {
    WinnerGenerationResponse generateWinners(GenerateWinnersRequest request);
}
