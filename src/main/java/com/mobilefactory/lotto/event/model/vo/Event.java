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
public class Event {
    private Long eventId;               // 이벤트ID (PK)
    private String eventName;           // 이벤트명
    private Date startDate;             // 이벤트 시작일
    private Date endDate;               // 이벤트 종료일
    private Date announceStart;         // 당첨자 발표 시작일
    private Date announceEnd;           // 당첨자 발표 종료일
    private Integer maxParticipants;    // 최대 참여자 수
    private Integer totalWinners;       // 총 당첨자 수
    private String winningNumbers;      // 당첨 번호 (콤마로 구분된 문자열, 예: "1,2,3,4,5,6")
    private String forcedWinnerPhone;   // 1등 지정 번호 (전화번호)
    private String status;              // 이벤트 상태 (예: ACTIVE, INACTIVE, ANNOUNCED)
    private Date createdAt;             // 생성 일시
    private Date updatedAt;             // 업데이트 일시
}
