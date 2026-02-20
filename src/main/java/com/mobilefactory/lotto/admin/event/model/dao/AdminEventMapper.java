package com.mobilefactory.lotto.admin.event.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mobilefactory.lotto.admin.event.model.dto.ForceWinnerGenerationRequest;
import com.mobilefactory.lotto.event.model.vo.Event;

@Mapper
public interface AdminEventMapper {
    int insertEvent(Event event);
    int updateForceWinnerPhone(ForceWinnerGenerationRequest request);
}
