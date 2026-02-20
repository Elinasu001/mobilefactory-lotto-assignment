package com.mobilefactory.lotto.admin.winner.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantUpdateVo {
    private Long participantId;   // 참가자ID (PK)
    private Integer prizeRank;    // 당첨 순위
    private String lottoNumbers;  // 당첨 번호 (예: "1,2,3,4,5,6")
}
