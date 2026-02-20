package com.mobilefactory.lotto.admin.event.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mobilefactory.lotto.admin.event.model.dto.ForceWinnerGenerationRequest;

@Mapper
public interface AdminEventMapper {
    int updateForceWinnerPhone(ForceWinnerGenerationRequest request);
}
