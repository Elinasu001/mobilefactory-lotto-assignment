package com.mobilefactory.lotto.event.model.dto;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventPublicResponse {
    private Long eventId;
    private String eventName;
    private Date startDate;
    private Date endDate;
    private Date announceStart;
    private Date announceEnd;
    private Integer maxParticipants;
    private Integer totalWinners;
    private String status;

    // 이벤트 참여 기간 여부 (이벤트 기간 중이면 true)
    private boolean eventActive;
    // 당첨자 발표 기간 여부 (당첨자 발표 기간 중이면 true)
    private boolean resultPeriod;
    // 접속 시 자동 팝업 오픈 여부
    // (이벤트 기간 또는 발표 기간일 경우 true)
    private boolean autoOpenPopup;
}
