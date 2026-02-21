package com.mobilefactory.lotto.admin.event.model.dto;

import java.sql.Date;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventRequest {

    @NotBlank(message = "이벤트명은 필수입니다.")
    private String eventName;

    @NotNull(message = "시작일은 필수입니다.")
    private Date startDate;

    @NotNull(message = "종료일은 필수입니다.")
    private Date endDate;

    @NotNull(message = "발표 시작일은 필수입니다.")
    private Date announceStart;

    @NotNull(message = "발표 종료일은 필수입니다.")
    private Date announceEnd;

    @Min(value = 1, message = "최대 참가자는 1명 이상이어야 합니다.")
    private Integer maxParticipants = 10000;

    @Min(value = 1, message = "총 당첨자는 1명 이상이어야 합니다.")
    private Integer totalWinners = 1000;

}
