package com.vandendaelen.openbackup.utils;

public class Timer {
    private long ticks;
    private long tickToReach;
    private static Timer INSTANCE;

    public Timer(long tickToReach) {
        this.ticks = 0;
        this.tickToReach = tickToReach;
        INSTANCE = this;
    }

    public static Timer getInstance() {
        return INSTANCE;
    }

    public boolean update(){
        if (++ticks >= tickToReach){
            ticks = 0;
            return true;
        }
        return false;
    }
}
