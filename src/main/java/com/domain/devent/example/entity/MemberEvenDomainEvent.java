package com.domain.devent.example.entity;

import com.domain.devent.common.DomainEvent;

/**
 * @author wuk
 * @description:
 * @menu
 * @date 2023/7/15 18:01
 */
public class MemberEvenDomainEvent extends  DomainEvent{

    private String memberName;

    private Integer memberAllMoney;

    private Integer frozenMoney;

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Integer getMemberAllMoney() {
        return memberAllMoney;
    }

    public void setMemberAllMoney(Integer memberAllMoney) {
        this.memberAllMoney = memberAllMoney;
    }

    public Integer getFrozenMoney() {
        return frozenMoney;
    }

    public void setFrozenMoney(Integer frozenMoney) {
        this.frozenMoney = frozenMoney;
    }
}
