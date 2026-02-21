package com.mobilefactory.lotto.admin.event.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mobilefactory.lotto.admin.event.model.dto.ForceWinnerGenerationRequest;
import com.mobilefactory.lotto.event.model.vo.Event;

@Mapper
public interface AdminEventMapper {

    // 이벤트 등록
    int insertEvent(Event event);
    // 1등 지정 번호 업데이트
    int updateForceWinnerPhone(ForceWinnerGenerationRequest request);

}
