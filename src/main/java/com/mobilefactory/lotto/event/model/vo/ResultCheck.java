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
public class ResultCheck {
    private Long checkId;           // 확인 ID (PK)
    private Long participantId;     // 참가자 ID (FK)
    private Integer checkCount;     // 확인 횟수 (1 or 2)
    private Date checkedAt;         // 확인 시간
}