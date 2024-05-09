package com.example.common.enums;

public enum StatusEnum {
    STATUS_OK("通过"),
    STATUS_NO("拒绝"),
    STATUS_WAIT("待审核"),

    SOLVE_YES("已解决"),
    SOLVE_NO("未解决"),
    ;

    public String status;
    StatusEnum(String status) {
        this.status = status;
    }
}
