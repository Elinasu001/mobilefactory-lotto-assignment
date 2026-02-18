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
    private Long eventId;
    private String eventName;
    private Date startDate;
    private Date endDate;
    private Date announceStart;
    private Date announceEnd;
    private Integer maxParticipants;
    private Integer totalWinners;
    private String status;
    private Date createdAt;
    private Date updatedAt;
}
