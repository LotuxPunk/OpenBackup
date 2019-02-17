package com.vandendaelen.openbackup.utils;

public enum EnumBroadcast {
    ALL(1),
    OP(2),
    NONE(3);

    private int value;

    EnumBroadcast(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
