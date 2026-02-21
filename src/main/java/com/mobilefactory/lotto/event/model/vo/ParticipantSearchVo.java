package com.mobilefactory.lotto.event.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantSearchVo {
    private Long eventId;       // 이벤트 ID(PK)
    private String phoneNumber; // 참여자 전화번호
    private Integer startNo;    // 시작 번호 (페이지네이션용)
    private Integer endNo;      // 종료 번호 (페이지네이션용)
}
