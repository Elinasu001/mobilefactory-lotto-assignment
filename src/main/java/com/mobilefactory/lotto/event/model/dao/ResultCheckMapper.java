package com.mobilefactory.lotto.event.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mobilefactory.lotto.event.model.vo.ResultCheck;

@Mapper
public interface ResultCheckMapper {

    // 확인 이력 조회
    ResultCheck selectByParticipantId(Long participantId);

    /**
     * 확인 이력 생성
     */
    int insertResultCheck(ResultCheck resultCheck);

    /**
     * 확인 횟수 업데이트
     */
    int updateCheckCount(ResultCheck resultCheck); 
}
