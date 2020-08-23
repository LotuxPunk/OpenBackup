package com.vandendaelen.openbackup.common.utils;

public class Timer {
    private long ticks;
    private long tickToReach;
    private static Timer INSTANCE;

    public Timer() {
        this.ticks = 0;
        this.tickToReach = 20000;
        INSTANCE = this;
    }

    public static Timer getInstance() {
        if (INSTANCE == null)
        {   INSTANCE = new Timer();
        }
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
