package com.mobilefactory.lotto.sms.model.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsLog {
    private Long smsId;
    private Long participantId;
    private String smsType;
    private String messageContent;
    private String sendStatus;
    private Date sentAt;
}
