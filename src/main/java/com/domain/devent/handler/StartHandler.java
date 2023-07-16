package com.domain.devent.handler;

import com.domain.devent.common.CustomerDomainEvent;
import com.domain.devent.common.DomainEvent;
import com.domain.devent.factory.EventDomainDisruptorFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.*;
import java.util.function.Consumer;

public class StartHandler {

    private final HashMap<String, ConsumerHandler> handlerHashMap = new HashMap<>();

    public <E extends DomainEvent> StartHandler workEventByDisruptor(String eventId, E event, Consumer<E> commandConsumer, Consumer<E> compensateConsumer) {
        //如果第一次今来
        //封装CustomerDomainEvent
        //如果存在，用旧的
        CustomerDomainEvent<DomainEvent> customerDomainEvent = (CustomerDomainEvent<DomainEvent>) new CustomerDomainEvent<E>();
        //创建DisruptorFactor
        EventDomainDisruptorFactory eventDomainDisruptorFactory = new EventDomainDisruptorFactory();
        Disruptor<CustomerDomainEvent<DomainEvent>> eventDisruptor = eventDomainDisruptorFactory.creatEventDomainFactory(customerDomainEvent);
        //处理事件
        ConsumerHandler eventConsumerHandler;
        if (handlerHashMap.containsKey(eventId)) {
            eventConsumerHandler = handlerHashMap.get(eventId);
        } else {
            eventConsumerHandler = new ConsumerHandler();
            handlerHashMap.put(eventId,eventConsumerHandler);
        }
        eventDisruptor.handleEventsWithWorkerPool(eventConsumerHandler);
        //运行订阅者
        eventDisruptor.start();

        //事件发布
        RingBuffer<CustomerDomainEvent<DomainEvent>> ringBuffer = eventDisruptor.getRingBuffer();
        long sequence = ringBuffer.next();
        CustomerDomainEvent<DomainEvent> domainEventCustomerDomainEvent = ringBuffer.get(sequence);
        domainEventCustomerDomainEvent.setEvent(event);
        domainEventCustomerDomainEvent.setEventCreateDate(new Date().getTime());
        domainEventCustomerDomainEvent.setEventId(eventId);
        domainEventCustomerDomainEvent.setCompensateConsumer((Consumer<DomainEvent>) compensateConsumer);
        domainEventCustomerDomainEvent.setCommandConsumer((Consumer<DomainEvent>) commandConsumer);
        ringBuffer.publish(sequence);
        eventDisruptor.shutdown();
        return this;
    }

}
