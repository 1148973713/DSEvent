package com.domain.devent.example.entity;

import com.domain.devent.common.DomainEvent;

public class TickEvenDomainEvent extends DomainEvent {

    private String tickId;

    private String tickName;


    private Integer tickNum;


    private Boolean tickStatus;


    private Integer tickFrozen;

    public String getTickId() {
        return tickId;
    }

    public void setTickId(String tickId) {
        this.tickId = tickId;
    }

    public String getTickName() {
        return tickName;
    }

    public void setTickName(String tickName) {
        this.tickName = tickName;
    }

    public Integer getTickNum() {
        return tickNum;
    }

    public void setTickNum(Integer tickNum) {
        this.tickNum = tickNum;
    }

    public Boolean getTickStatus() {
        return tickStatus;
    }

    public void setTickStatus(Boolean tickStatus) {
        this.tickStatus = tickStatus;
    }

    public Integer getTickFrozen() {
        return tickFrozen;
    }

    public void setTickFrozen(Integer tickFrozen) {
        this.tickFrozen = tickFrozen;
    }
}
