package com.mobilefactory.lotto.event.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mobilefactory.lotto.event.model.vo.Participant;
import com.mobilefactory.lotto.event.model.vo.ParticipantSearchVo;

@Mapper
public interface ParticipantMapper {
    boolean existsByEventAndPhone(ParticipantSearchVo searchVo);
    int getNextParticipantNo(Long eventId);
    int insertParticipant(Participant participant);
}
