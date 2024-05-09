package com.example.common.enums;

public enum OrderStatusEnum {
    UNPAID("待支付"),
    UNSHIPPED("待发货"),
    UNRECEIVED("待收货"),
    RECEIVED("已完成"),
    CANCELLED("已取消"),
    ;

    public String value;

    OrderStatusEnum(String value) {
        this.value = value;
    }

}
