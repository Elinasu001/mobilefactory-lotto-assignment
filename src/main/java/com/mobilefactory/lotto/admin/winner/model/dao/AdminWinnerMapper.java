package com.mobilefactory.lotto.admin.winner.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mobilefactory.lotto.admin.winner.model.vo.EventUpdateVo;
import com.mobilefactory.lotto.admin.winner.model.vo.ParticipantUpdateVo;
import com.mobilefactory.lotto.event.model.vo.Participant;
import com.mobilefactory.lotto.event.model.vo.ParticipantSearchVo;

@Mapper
public interface AdminWinnerMapper {

    // 당첨 번호 업데이트
    int updateWinningNumbers(EventUpdateVo updateVo);

    // 당첨자 존재 여부 확인
    boolean existsWinners(Long eventId);

    // 1등 지정 번호로 참가자 조회
    Participant selectByPhone(ParticipantSearchVo searchVo);

    // 참가자 번호 범위로 조회 (2등, 3등)
    List<Participant> selectByParticipantNoRange(ParticipantSearchVo searchVo);

    // 미당첨자 조회 (4등)
    List<Participant> selectNonWinners(Long eventId);

    // 당첨 정보 업데이트
    int updateWinner(ParticipantUpdateVo updateVo);

    // 이벤트 상태 업데이트
    int updateEventStatus(EventUpdateVo updateVo);
}
