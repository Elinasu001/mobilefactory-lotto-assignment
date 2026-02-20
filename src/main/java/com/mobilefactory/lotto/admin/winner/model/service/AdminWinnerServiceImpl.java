package com.mobilefactory.lotto.admin.winner.model.service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mobilefactory.lotto.admin.winner.model.dao.AdminWinnerMapper;
import com.mobilefactory.lotto.admin.winner.model.dto.GenerateWinnersRequest;
import com.mobilefactory.lotto.admin.winner.model.dto.WinnerGenerationResponse;
import com.mobilefactory.lotto.admin.winner.model.vo.EventUpdateVo;
import com.mobilefactory.lotto.admin.winner.model.vo.ParticipantUpdateVo;
import com.mobilefactory.lotto.common.exception.event.EventNotFoundException;
import com.mobilefactory.lotto.common.exception.winner.ForcedWinnerNotFoundException;
import com.mobilefactory.lotto.common.exception.winner.WinnerAlreadyGeneratedException;
import com.mobilefactory.lotto.event.model.dao.EventMapper;
import com.mobilefactory.lotto.event.model.vo.Event;
import com.mobilefactory.lotto.event.model.vo.Participant;
import com.mobilefactory.lotto.event.model.vo.ParticipantSearchVo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminWinnerServiceImpl implements AdminWinnerService {

    private final EventMapper eventMapper;
    private final AdminWinnerMapper adminWinnerMapper;

    @Override
    @Transactional
    public WinnerGenerationResponse generateWinners(GenerateWinnersRequest request) {

        Long eventId = request.getEventId();
        String forcedWinnerPhone = request.getForcedWinnerPhone();

        //log.info("당첨자 생성 시작");
        //log.info("이벤트 ID: {}", eventId);
        //log.info("1등 지정 번호: {}", forcedWinnerPhone);

        // 1. 이벤트 존재 확인
        Event activeEvent = eventMapper.selectActiveEvent();
        if(activeEvent == null){
            throw new EventNotFoundException("진행중인 이벤트가 없습니다.");
        }
        //log.info("현재 진행중인 이벤트 : {} (ID: {})", activeEvent.getEventName(), activeEvent.getEventId());

        // 2. 이미 당첨자가 생성되었는지 확인
        boolean winnersExist = adminWinnerMapper.existsWinners(eventId);

        if(winnersExist){
            throw new WinnerAlreadyGeneratedException("이미 당첨자가 생성되었습니다.");
        }
        //log.info("당첨자 생성 여부: {}", winnersExist);


        //log.info("== 1등 지정 번호 조회 ==");

        // 3. 1등 지정 번호 참가자 번호인지 조회
        Participant rank1Winner = adminWinnerMapper.selectByPhone(
            ParticipantSearchVo.builder()
                .eventId(eventId)
                .phoneNumber(forcedWinnerPhone)
                .build()
        );

        if(rank1Winner == null){
            throw new ForcedWinnerNotFoundException("해당 이벤트에 참여하지 않은 전화번호는 1등으로 지정할 수 없습니다. 전화번호: " + forcedWinnerPhone);
        }

        // 4. 1등의 로또 번호를 정답으로 설정
        String winningNumbers = rank1Winner.getLottoNumbers();
        //log.info("1등 참가자 로또 번호 : {}", winningNumbers);
        //log.info ("== 확정! ==");

        // 5. 이벤트 테이블에 정답 번호 저장
        int updatedWinningNumbers = adminWinnerMapper.updateWinningNumbers(
            EventUpdateVo.builder()
                .eventId(eventId)
                .winningNumbers(winningNumbers)
                .build()
        );
        //log.info("업데이트 여부 : {}", updatedWinningNumbers);

        if(updatedWinningNumbers == 0){
            throw new RuntimeException("당첨 번호 업데이트에 실패했습니다.");
        }

        // 6. 1등 처리
        int updatedRank1Winner = adminWinnerMapper.updateWinner(
            ParticipantUpdateVo.builder()
                .participantId(rank1Winner.getParticipantId())
                .prizeRank(1)
                .lottoNumbers(rank1Winner.getLottoNumbers())
                .build()
        );
        //log.info("업데이트 여부 : {}", updatedRank1Winner);
        //log.info("1등 확정: 참가번호 {}", rank1Winner.getParticipantNo());

        if(updatedRank1Winner == 0){
            throw new RuntimeException("1등 참가자 업데이트에 실패했습니다.");
        }

        int rank1Count = 1;
        int rank2Count = 0;
        int rank3Count = 0;
        int rank4Count = 0;

        // 7. 2등 선정
        //log.info("== 2등 선정 ==");
        List<Participant> rank2Candidates  = adminWinnerMapper.selectByParticipantNoRange(
            ParticipantSearchVo.builder()
                .eventId(eventId)
                .startNo(2000)
                .endNo(7000)
                .build()
        );
        //log.info("2등 구간 후보자 수: {}", rank2Candidates.size());

        // 7-1. 2등 5자리 일치 후보자 필터링
        List<Participant> rank2Matches = rank2Candidates.stream()
            .filter(p -> countMatches(winningNumbers, p.getLottoNumbers()) == 5)
            .limit(5)
            .collect(Collectors.toList());


        //log.info("5자리 일치자 수: {}", rank2Matches.size());

        // if(rank2Matches.size() < 5){
        //     throw new NotEnoughCandidatesException("2등 후보자가 부족합니다. (필요: 5명, 현재: " + rank2Matches.size() + "명)");
        // }

        // 7-3. 2등 당첨자 업데이트
        for(Participant winner : rank2Matches){
            adminWinnerMapper.updateWinner(
                ParticipantUpdateVo.builder()
                    .participantId(winner.getParticipantId())
                    .prizeRank(2)
                    .lottoNumbers(winner.getLottoNumbers())
                    .build()
            );
            rank2Count++;
        }
        //log.info("2등 선정 완료: {}명", rank2Matches.size());

        // 8. 3등 선정
        //log.info("== 3등 선정 ==");
        List<Participant> rank3Candidates = adminWinnerMapper.selectByParticipantNoRange(
            ParticipantSearchVo.builder()
                .eventId(eventId)
                .startNo(1000)
                .endNo(8000)
                .build()
        );
        //log.info("3등 구간 후보자 수: {}", rank3Candidates.size());

        // 8-1. 3등 4자리 일치 후보자 필터링
        List<Participant> rank3Matches = rank3Candidates.stream()
            .filter(p -> countMatches(winningNumbers, p.getLottoNumbers()) == 4)
            .limit(44)
            .collect(Collectors.toList());
        //log.info("4자리 일치자 수: {}", rank3Matches.size());

        // if(rank3Matches.size() < 44){
        //     throw new NotEnoughCandidatesException("3등 후보자가 부족합니다. (필요: 44명, 현재: " + rank3Matches.size() + "명)");
        // }

        // 8-2. 3등 당첨자 업데이트
        for (Participant winner : rank3Matches) {
            adminWinnerMapper.updateWinner(
                ParticipantUpdateVo.builder()
                    .participantId(winner.getParticipantId())
                    .prizeRank(3)
                    .lottoNumbers(winner.getLottoNumbers())
                    .build()
            );
            rank3Count++;
        }
        //log.info("3등 선정 완료: {}명", rank3Matches.size());

        // 9. 4등 선정
        //log.info("== 4등 선정 ==");
        List<Participant> rank4Candidates = adminWinnerMapper.selectNonWinners(eventId);
        //log.info("4등 후보자 수: {}", rank4Candidates.size());

        // 9-1. 4등 3자리 이상 일치 후보자 필터링
        List<Participant> rank4Matches = rank4Candidates.stream()
            .filter(p -> countMatches(winningNumbers, p.getLottoNumbers()) >= 3)
            .limit(950)
            .collect(Collectors.toList());

        //log.info("3자리 이상 일치자 수: {}", rank4Matches.size());

        // if (rank4Matches.size() < 950) {
        //     throw new NotEnoughCandidatesException(
        //         "4등 후보자가 부족합니다. (필요: 950명, 현재: " + rank4Matches.size() + "명)"
        //     );
        // }

        // 9-2. 4등 당첨자 업데이트
        for (Participant winner : rank4Matches) {
            adminWinnerMapper.updateWinner(
                ParticipantUpdateVo.builder()
                    .participantId(winner.getParticipantId())
                    .prizeRank(4)
                    .lottoNumbers(winner.getLottoNumbers())
                    .build()
            );
            rank4Count++;
        }
        //log.info("4등 선정 완료: {}명", rank4Matches.size());

        //log.info("당첨자 생성 완료");
        //log.info("정답 번호: {}", winningNumbers);
        //log.info("1등: {}명, 2등: {}명, 3등: {}명, 4등: {}명", rank1Count, rank2Count, rank3Count, rank4Count);

        WinnerGenerationResponse response = WinnerGenerationResponse.builder()
            .eventId(eventId)
            .totalWinners(rank1Count + rank2Count + rank3Count + rank4Count)
            .rank1Count(rank1Count)
            .rank2Count(rank2Count)
            .rank3Count(rank3Count)
            .rank4Count(rank4Count)
            .build();

        return response;
    }

    /**
     * 두 로또 번호 일치 개수 계산
     **/
    private int countMatches(String winningNumbers, String lottoNumbers) {

        Set<Integer> winningSet = Arrays.stream(winningNumbers.split(","))
            .map(String::trim)      // 공백 제거
            .map(Integer::parseInt) // 문자열 -> 숫자 변환
            .collect(Collectors.toSet()); // Set은 내부적으로 Hash 구조를 사용

        Set<Integer> lottoSet = Arrays.stream(lottoNumbers.split(","))
            .map(String::trim)
            .map(Integer::parseInt)
            .collect(Collectors.toSet());

        // 겹치는 것만 남기기 (교집합)
        winningSet.retainAll(lottoSet);

        // 남은 개수 = 일치 개수
        return winningSet.size();
    }


}
