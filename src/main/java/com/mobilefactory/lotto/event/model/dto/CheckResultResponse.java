package com.mobilefactory.lotto.event.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckResultResponse {

    private Long eventId;
    private String eventName;
    private String phoneNumber;
    //private String lottoNumbers;
    //private String winningNumbers;

    private Boolean isWinner;           // 당첨 여부
    private Integer prizeRank;          // 등수 (1, 2, 3, 4) - 최초 확인 시만
    private Integer checkCount;         // 확인 횟수 (1 or 2)
    private String checkMessage;        // 안내 메시지
}