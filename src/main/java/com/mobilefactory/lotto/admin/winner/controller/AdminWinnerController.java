package com.mobilefactory.lotto.admin.winner.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mobilefactory.lotto.admin.winner.model.dto.GenerateWinnersRequest;
import com.mobilefactory.lotto.admin.winner.model.dto.WinnerGenerationResponse;
import com.mobilefactory.lotto.admin.winner.model.service.AdminWinnerService;
import com.mobilefactory.lotto.common.ResponseData;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/admin/winners")
@RequiredArgsConstructor
public class AdminWinnerController {

    private final AdminWinnerService winnerService;

    /**
     * 당첨자 생성
     */
    @PostMapping("/generate")
    public ResponseEntity<ResponseData<WinnerGenerationResponse>> generateWinners(
            @Valid @RequestBody GenerateWinnersRequest request) {

        WinnerGenerationResponse response = winnerService.generateWinners(request);

        return ResponseData.created(response, "당첨자 생성이 완료되었습니다.");
    }

}
