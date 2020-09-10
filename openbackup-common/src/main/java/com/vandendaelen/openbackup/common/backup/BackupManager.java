package com.vandendaelen.openbackup.common.backup;

import com.vandendaelen.openbackup.common.threads.BackupThread;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
    private boolean dirty;
    private Runnable disableWorldSaving;
    private Runnable enableWorldSaving;

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

    public void init(int maxFileToKeep, long tickToReach, Path gameDir, Path openBackupDir, String worldname, boolean client, Runnable disableWorldSaving, Runnable enableWorldSaving){
        this.maxFileToKeep = maxFileToKeep;
        this.tickToReach = tickToReach;
        this.gameDir = gameDir;
        this.openBackupDir = openBackupDir;
        this.worldname = worldname;
        this.client = client;
        this.disableWorldSaving = disableWorldSaving;
        this.enableWorldSaving = enableWorldSaving;

        try {
            Files.createDirectory(openBackupDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            disableWorldSaving.run();
            BackupThread thread = new BackupThread(this.gameDir, this.openBackupDir, this.worldname, this.maxFileToKeep, this.client, () -> () -> {
                running = false;
                enableWorldSaving.run();
            });

            thread.start();
        }
        else {
            //TODO : Throw Exception
        }
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
}
