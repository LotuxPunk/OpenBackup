package com.vandendaelen.openbackup.common.backup;

import com.vandendaelen.openbackup.common.threads.BackupThread;

import java.nio.file.Path;
import java.util.function.Supplier;

public class BackupManager {
    private static BackupManager INSTANCE;
    private int maxFileToKeep;
    private long tickToReach;
    private Path gameDir;
    private Path openBackupDir;
    private String worldname;
    private boolean client;
    private boolean running;
    private long currentTick;

    public BackupManager() {
        this.running = false;
        this.currentTick = 0;
    }

    public static BackupManager getInstance(){
        if (INSTANCE == null) {
            synchronized (BackupManager.class){
                if(INSTANCE == null){
                    INSTANCE = new BackupManager();
                }
            }
        }
        return INSTANCE;
    }

    public void init(int maxFileToKeep, long tickToReach, Path gameDir, Path openBackupDir, String worldname, boolean client){
        this.maxFileToKeep = maxFileToKeep;
        this.tickToReach = tickToReach;
        this.gameDir = gameDir;
        this.openBackupDir = openBackupDir;
        this.worldname = worldname;
        this.client = client;
    }

    public void tick(){
        this.currentTick++;
        if (this.currentTick >= this.tickToReach){
            this.createBackup();
            this.currentTick = 0;
        }
    }

    public void setTickToReach(long tickToReach) {
        this.tickToReach = tickToReach;
    }

    public void setMaxFileToKeep(int maxFileToKeep) {
        this.maxFileToKeep = maxFileToKeep;
    }

    public void createBackup(){
        if (!this.running){
            this.running = true;
            //TODO : Disable world saving
            BackupThread thread = new BackupThread(this.gameDir, this.openBackupDir, this.worldname, this.maxFileToKeep, this.client, new Supplier<Runnable>() {
                @Override
                public Runnable get() {
                    return () -> {
                        running = false;
                        //TODO : Enable world saving + logging
                    };
                }
            });
        }
        else {
            //TODO : Throw Exception
        }
    }
}
