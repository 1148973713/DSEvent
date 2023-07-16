package com.domain.devent.handler;

import com.domain.devent.common.CompensateDomainEvent;
import com.domain.devent.common.CompensateEvent;
import com.domain.devent.common.CustomerDomainEvent;
import com.domain.devent.common.DomainEvent;
import com.domain.devent.factory.EventDomainDisruptorFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.*;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

public class ConsumerHandler implements WorkHandler<CustomerDomainEvent<DomainEvent>> {

    private final HashMap<String, ReplyHandler> handlerHashMap = new HashMap<>();

    private final HashMap<String, Boolean> hasErrorHashMap = new HashMap<>();

    //监听到传过来的类和方法
    @Override
    public void onEvent(CustomerDomainEvent<DomainEvent> customerDomainEvent) {
        String eventId = customerDomainEvent.getEventId();
        DomainEvent event = customerDomainEvent.getEvent();
        Consumer<DomainEvent> compensateConsumer = customerDomainEvent.getCompensateConsumer();
        Boolean result = hasErrorHashMap.get(eventId);
        if (result == null || !result) {
            try {
                hasErrorHashMap.put(eventId, false);
                customerDomainEvent.getCommandConsumer().accept(event);
                getReply(customerDomainEvent, eventId, event, compensateConsumer, false);
            } catch (Exception e) {
                hasErrorHashMap.replace(eventId, true);
                if (customerDomainEvent.getCompensateConsumer() != null) {
                    getReply(customerDomainEvent, eventId, event, compensateConsumer, true);
                }
            }
        }
    }

    private void getReply(CustomerDomainEvent<DomainEvent> customerDomainEvent, String eventId, DomainEvent event, Consumer<DomainEvent> compensateConsumer, Boolean result) {
        CustomerDomainEvent<DomainEvent> domainEvent = new CustomerDomainEvent<>();
        EventDomainDisruptorFactory eventDomainDisruptorFactory = new EventDomainDisruptorFactory();
        Disruptor<CustomerDomainEvent<DomainEvent>> eventDisruptor = eventDomainDisruptorFactory.creatEventDomainFactory(domainEvent);
        ReplyHandler replyHandler;
        if (handlerHashMap.containsKey(eventId)) {
            replyHandler = handlerHashMap.get(eventId);
        } else {
            replyHandler = new ReplyHandler();
            handlerHashMap.put(eventId, replyHandler);
        }
        eventDisruptor.handleEventsWithWorkerPool(replyHandler);
        //运行事件
        eventDisruptor.start();

        RingBuffer<CustomerDomainEvent<DomainEvent>> ringBuffer = eventDisruptor.getRingBuffer();
        long sequence = ringBuffer.next();
        CustomerDomainEvent<DomainEvent> domainEventCustomerDomainEvent = ringBuffer.get(sequence);

        Long eventCreateDate = customerDomainEvent.getEventCreateDate();
        CompensateEvent compensateEvent = new CompensateEvent(event, compensateConsumer, eventCreateDate);

        //放入数组中
        domainEventCustomerDomainEvent.setEvent(event);
        domainEventCustomerDomainEvent.setEventCreateDate(eventCreateDate);
        domainEventCustomerDomainEvent.setEventErrorStatus(result);
        domainEventCustomerDomainEvent.setEventId(eventId);
        domainEventCustomerDomainEvent.setCompensateConsumer(compensateConsumer);
        domainEventCustomerDomainEvent.setNewCompensateConsumer(compensateEvent);
        ringBuffer.publish(sequence);
        eventDisruptor.shutdown();
    }
}
