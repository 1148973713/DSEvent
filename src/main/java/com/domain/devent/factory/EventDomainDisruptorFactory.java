package com.domain.devent.factory;

import com.domain.devent.common.CustomerDomainEvent;
import com.domain.devent.common.DomainEvent;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ThreadFactory;

public class EventDomainDisruptorFactory {
    //设置ringBuff大小
    private static final int RING_BUFFER_SIZE = 1024 * 8;

    private static final String EVENT_DOMAIN = "event_domain";

    private final HashMap<String, ThreadFactory> factoryHashMap = new HashMap<>();

    public <C extends DomainEvent> Disruptor<CustomerDomainEvent<C>> creatEventDomainFactory(CustomerDomainEvent<C> customerEventDomain) {
        //创建新的线程对象
        EventFactory<CustomerDomainEvent<C>> eventFactory = () -> customerEventDomain;
        //放入Map中搜索
        String className;
        if (StringUtils.isEmpty(customerEventDomain.getEventName())){
            className = customerEventDomain.getEventName();
        }else {
            className = customerEventDomain.getEvent().getClass().getSimpleName();
        }

        if ( factoryHashMap.containsKey(className)) {
            ThreadFactory factory = factoryHashMap.get(className);
            //当 EventFactory的newInstance() 方法被调用时，lambda 表达式会返回一个预先存在的对象，作为事件对象的新实例。
            return new Disruptor<>(eventFactory, RING_BUFFER_SIZE, factory, ProducerType.SINGLE, new YieldingWaitStrategy());
        } else {
            ThreadFactory factory = new ThreadFactory() {
                @Override
                public Thread newThread(Runnable runnable) {
                    String threadName = EVENT_DOMAIN + "_" + UUID.randomUUID().toString().replace("-", "");
                    return new Thread(runnable, threadName);
                }
            };
            factoryHashMap.put(className, factory);
            return new Disruptor<>(eventFactory, RING_BUFFER_SIZE, factory, ProducerType.SINGLE, new YieldingWaitStrategy());
        }
    }
}
