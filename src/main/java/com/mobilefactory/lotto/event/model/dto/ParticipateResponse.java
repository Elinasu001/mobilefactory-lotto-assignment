package com.mobilefactory.lotto.event.model.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipateResponse {
    private Long participantId;     // 참가자ID (PK)
    private Integer participantNo;  // 이벤트 내에서의 참가자 순번
    private String lottoNumbers;    // 로또 번호
    private Date participatedAt;    // 참여 일시
}
