package com.mobilefactory.lotto.event.util;

import org.springframework.stereotype.Component;

import com.mobilefactory.lotto.common.exception.event.EventNotFoundException;
import com.mobilefactory.lotto.event.model.dao.EventMapper;
import com.mobilefactory.lotto.event.model.vo.Event;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventValidator {

    private final EventMapper eventMapper;

    // 이벤트 상태 상수
    public static final String ACTIVE = "ACTIVE";
    public static final String CLOSED = "CLOSED";
    public static final String ANNOUNCED = "ANNOUNCED";

    /**
     * ID로 이벤트 조회 (상태 무관)
     */
    public Event getEventById(Long eventId) {
        Event event = eventMapper.selectById(eventId);

        if (event == null) {
            throw new EventNotFoundException("이벤트를 찾을 수 없습니다.");
        }

        return event;
    }

    /**
     * 특정 상태의 이벤트 조회
     */
    public Event getEventByStatus(Long eventId, String status) {
        Event event = getEventById(eventId);

        if (!status.equals(event.getStatus())) {
            throw new IllegalStateException(
                String.format("상태가 %s인 이벤트가 아닙니다. (현재: %s)",
                    status, event.getStatus())
            );
        }
        return event;
    }

    /**
     * ACTIVE 이벤트 조회 (자주 사용)
     */
    public Event getActiveEvent(Long eventId) {
        return getEventByStatus(eventId, ACTIVE);
    }

    /**
     * CLOSED 이벤트 조회
     */
    public Event getClosedEvent(Long eventId) {
        return getEventByStatus(eventId, CLOSED);
    }

    /**
     * ANNOUNCED 이벤트 조회
     */
    public Event getAnnouncedEvent(Long eventId) {
        return getEventByStatus(eventId, ANNOUNCED);
    }
}