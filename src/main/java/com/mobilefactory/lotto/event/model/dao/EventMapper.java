package com.mobilefactory.lotto.event.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mobilefactory.lotto.event.model.vo.Event;

@Mapper
public interface EventMapper {
    // 현재 진행중인 이벤트 조회
    Event selectActiveEvent();
    // 이벤트 ID로 조회
    Event selectById(Long eventId);

    // 스케줄러 - 만료된 이벤트 마감 처리
    int closeExpiredEvents();
    // 마감된 이벤트 조회
    Event selectClosedEvent(Long eventId);
}
