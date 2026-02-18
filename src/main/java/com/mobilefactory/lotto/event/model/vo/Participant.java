package com.mobilefactory.lotto.event.model.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Participant {
    private Long participantId;     // 참가아ID (PK)
    private Long eventId;           // 이벤트ID (FK)
    private String phoneNumber;     // 전화번호
    private Integer participantNo;  // 이벤트 내에서의 참가자 순번
    private String lottoNumbers;    // 당첨 번호 (6자리, 콤마 구분)
    private String isWinner;        // 당첨 여부 (Y/N)
    private Integer prizeRank;      // 당첨 순위
    private Date participatedAt;    // 참여 일시
    private Date createdAt;         // 생성 일시
}
