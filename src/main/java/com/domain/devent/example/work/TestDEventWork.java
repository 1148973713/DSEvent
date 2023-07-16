package com.domain.devent.example.work;

import com.domain.devent.handler.StartHandler;
import com.domain.devent.example.entity.TickEvenDomainEvent;
import com.domain.devent.example.entity.MemberEvenDomainEvent;

import java.util.UUID;

public class TestDEventWork {

    private static Integer result;

    private static Integer allMoney;

    private void memberDomainEventVoid(MemberEvenDomainEvent memberEvenDomainEvent) {
        //String tickId = createTickEvenDomainEven;
        allMoney = memberEvenDomainEvent.getMemberAllMoney() - 10;
        System.out.println(memberEvenDomainEvent.getMemberName() +"扣了10元，存款剩余:" + allMoney);
    }

    private void memberCompensateDomainEventVoid(MemberEvenDomainEvent memberEvenDomainEvent) {
        //String tickId = createTickEvenDomainEven;
        allMoney = allMoney + 10;
        System.out.println(memberEvenDomainEvent.getMemberName() + "账户回滚成功，存款剩余:" + allMoney);
    }

    private void TickDomainEventVoid(TickEvenDomainEvent tickEvenDomainEvent) {
        //String tickId = createTickEvenDomainEven;
        result = tickEvenDomainEvent.getTickNum() - 1;
        String tickName = tickEvenDomainEvent.getTickName();
        System.out.println(tickName + ":正在减库存:" + result);
        int i = 10 / 0;
    }

    private void TickCompensateDomainEventVoid(TickEvenDomainEvent tickEvenDomainEvent) {
        result = result + 1;
        String tickName = tickEvenDomainEvent.getTickName();
        System.out.println(tickName + ":库存回滚操作成功" + result);
    }

    public void sagaStatus() {
        //用户余额
        MemberEvenDomainEvent memberEvenDomainEvent = new MemberEvenDomainEvent();
        memberEvenDomainEvent.setMemberAllMoney(1000);
        memberEvenDomainEvent.setMemberName("张三");
        //订单锁库存
        TickEvenDomainEvent tickEvenDomainEvent = new TickEvenDomainEvent();
        tickEvenDomainEvent.setTickId("1111");
        tickEvenDomainEvent.setTickName("饼干");
        tickEvenDomainEvent.setTickNum(180);
        tickEvenDomainEvent.setTickStatus(false);
        //创建DisruptorHandler
        String eventId = UUID.randomUUID().toString();

        StartHandler memberEvenDomainEventStartHandler = new StartHandler();
        memberEvenDomainEventStartHandler
                .workEventByDisruptor(eventId, memberEvenDomainEvent, this::memberDomainEventVoid, this::memberCompensateDomainEventVoid)
                .workEventByDisruptor(eventId, tickEvenDomainEvent, this::TickDomainEventVoid, this::TickCompensateDomainEventVoid);
    }

    public static void main(String[] args) {
        TestDEventWork testDEventWork = new TestDEventWork();
        testDEventWork.sagaStatus();
    }
}
