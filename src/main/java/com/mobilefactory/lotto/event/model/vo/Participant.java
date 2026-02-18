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
    private Long participantId;
    private Long eventId;
    private String phoneNumber;
    private Integer participantNo;
    private String lottoNumbers;
    private String isWinner;
    private Integer prizeRank;
    private Date participatedAt;
    private Date createdAt;
}
