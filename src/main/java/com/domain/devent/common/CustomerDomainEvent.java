package com.domain.devent.common;

import com.domain.devent.eunm.EventType;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class CustomerDomainEvent<T extends DomainEvent> {
    private String eventId;

    private Long eventCreateDate;

    private Boolean eventErrorStatus;

    private T event;

    private String eventName;

    private Consumer<T> commandConsumer;

    private Consumer<T> compensateConsumer;

    private CompensateEvent newCompensateConsumer;

    public CustomerDomainEvent() {
        // 私有化构造函数，限制通过new关键字直接创建实例
    }

    public CustomerDomainEvent(String eventId, T event, Consumer<T> commandConsumer, Consumer<T> compensateConsumer) {
        this.eventId = eventId;
        this.eventCreateDate = new Date().getTime();
        this.event = event;
        this.commandConsumer = commandConsumer;
        this.compensateConsumer = compensateConsumer;
        this.eventErrorStatus = false;
    }

    // Getters and setters

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Long getEventCreateDate() {
        return eventCreateDate;
    }

    public void setEventCreateDate(Long eventCreateDate) {
        this.eventCreateDate = eventCreateDate;
    }

    public Boolean getEventErrorStatus() {
        return eventErrorStatus;
    }

    public void setEventErrorStatus(Boolean eventErrorStatus) {
        this.eventErrorStatus = eventErrorStatus;
    }

    public T getEvent() {
        return event;
    }

    public void setEvent(T event) {
        this.event = event;
    }

    public Consumer<T> getCommandConsumer() {
        return commandConsumer;
    }

    public void setCommandConsumer(Consumer<T> commandConsumer) {
        this.commandConsumer = commandConsumer;
    }

    public Consumer<T> getCompensateConsumer() {
        return compensateConsumer;
    }

    public void setCompensateConsumer(Consumer<T> compensateConsumer) {
        this.compensateConsumer = compensateConsumer;
    }

    public String getEventName() {
        return this.eventName;
    }

    public void setEventName() {
        this.eventName = this.getEvent().getClass().getSimpleName();
    }


    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public CompensateEvent getNewCompensateConsumer() {
        return newCompensateConsumer;
    }

    public void setNewCompensateConsumer(CompensateEvent newCompensateConsumer) {
        this.newCompensateConsumer = newCompensateConsumer;
    }
}
