package com.mobilefactory.lotto.event.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mobilefactory.lotto.event.model.vo.Event;

@Mapper
public interface EventMapper {
    Event selectActiveEvent();
    Event selectById(Long eventId);
}
