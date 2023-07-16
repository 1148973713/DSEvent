package com.domain.devent.handler;

import com.domain.devent.common.CompensateEvent;
import com.domain.devent.common.CustomerDomainEvent;
import com.domain.devent.common.DomainEvent;
import com.lmax.disruptor.WorkHandler;

import java.util.*;
import java.util.function.Consumer;

public class ReplyHandler implements WorkHandler<CustomerDomainEvent<DomainEvent>> {

    private final List<CompensateEvent> consumerList = new ArrayList<>();

    @Override
    public void onEvent(CustomerDomainEvent<DomainEvent> customerDomainEvent) throws Exception {
        consumerList.add(customerDomainEvent.getNewCompensateConsumer());
        //System.out.println(customerDomainEvent.getEventName()+"回滚方法："+consumerList.size());

        if (customerDomainEvent.getEventErrorStatus()){
            consumerList.stream().sorted(Comparator.comparing(CompensateEvent::getEventCreateDate).reversed())
                    .forEach(compensateEvent -> {
                        DomainEvent event = compensateEvent.getEvent();
                        compensateEvent.getCompensateConsumer().accept(event);
                    });
        }
/*        DomainEvent event = customerDomainEvent.getEvent();
        customerDomainEvent.getCompensateConsumer().accept(event);
        consumerMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByKey()))
                .forEach(domainEvent->{
                    Consumer<DomainEvent> value = domainEvent.getValue();

                });*/
    }
}
