package com.mobilefactory.lotto.event.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mobilefactory.lotto.event.model.vo.Participant;
import com.mobilefactory.lotto.event.model.vo.ParticipantSearchVo;

@Mapper
public interface ParticipantMapper {
    // 이벤트 참여 여부 확인
    boolean existsByEventAndPhone(ParticipantSearchVo searchVo);
    // 다음 참가자 번호 조회
    int getNextParticipantNo(Long eventId);
    // 참가자 정보 삽입
    int insertParticipant(Participant participant);
    // 참가자 조회
    Participant selectByPhone(ParticipantSearchVo searchVo);

}
