package com.mobilefactory.lotto.admin.winner.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WinnerGenerationResponse {
    private Long eventId;           // 이벤트ID (PK)
    private Integer totalWinners;   // 총 당첨자 수
    private Integer rank1Count;     // 1등 당첨자 수
    private Integer rank2Count;     // 2등 당첨자 수
    private Integer rank3Count;     // 3등 당첨자 수
    private Integer rank4Count;     // 4등 당첨자 수
}
