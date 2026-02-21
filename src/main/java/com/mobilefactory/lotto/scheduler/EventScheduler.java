package com.mobilefactory.lotto.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mobilefactory.lotto.event.model.dao.EventMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventScheduler {

    private final EventMapper eventMapper;

    // 매일 자정에 실행
    // 테스트용 서버 시작하자마자 바로 1번 실행
    // @Scheduled(fixedDelay = Long.MAX_VALUE, initialDelay = 0)
    @Scheduled(cron = "0 0 0 * * *")
    public void closeExpiredEvents() {
        log.info("이벤트 마감 스케줄러 실행");

        int closedCount = eventMapper.closeExpiredEvents();

        log.info("마감 처리된 이벤트 수: {}", closedCount);
    }

}
