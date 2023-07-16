package com.domain.devent.common;

import java.util.function.Consumer;

/**
 * @author wuk
 * @description:
 * @menu
 * @date 2023/7/15 17:31
 */
public class CompensateDomainEvent<T> {


    private Long time;

    private Consumer<T> compensateConsumer;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Consumer<T> getCompensateConsumer() {
        return compensateConsumer;
    }

    public void setCompensateConsumer(Consumer<T> compensateConsumer) {
        this.compensateConsumer = compensateConsumer;
    }
}
