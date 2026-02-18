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
    private Long participantId;
    private Integer participantNo;
    private String lottoNumbers;
    private Date participatedAt;
}
