package com.mobilefactory.lotto.admin.winner.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventUpdateVo {
    private Long eventId;           // 이벤트ID (PK)
    private String winningNumbers;  // 당첨 번호 (예: "1,2,3,4,5,6")
    private String status;          // 이벤트 상태 (예: "CLOSED", "ANNOUNCED")
}
