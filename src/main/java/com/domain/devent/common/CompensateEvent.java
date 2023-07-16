package com.domain.devent.common;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author wuk
 * @description:
 * @menu
 * @date 2023/7/16 13:19
 */
public class CompensateEvent {
    private DomainEvent event;

    private Consumer<DomainEvent> compensateConsumer;

    private Long eventCreateDate;

    public DomainEvent getEvent() {
        return event;
    }

    public void setEvent(DomainEvent event) {
        this.event = event;
    }

    public Consumer<DomainEvent> getCompensateConsumer() {
        return compensateConsumer;
    }

    public void setCompensateConsumer(Consumer<DomainEvent> compensateConsumer) {
        this.compensateConsumer = compensateConsumer;
    }

    public Long getEventCreateDate() {
        return eventCreateDate;
    }

    public void setEventCreateDate(Long eventCreateDate) {
        this.eventCreateDate = eventCreateDate;
    }

    public CompensateEvent(DomainEvent event, Consumer<DomainEvent> compensateConsumer, Long eventCreateDate) {
        this.event = event;
        this.compensateConsumer = compensateConsumer;
        this.eventCreateDate = eventCreateDate;
    }
}
