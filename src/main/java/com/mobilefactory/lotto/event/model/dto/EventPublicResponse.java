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
    private String status;
}
