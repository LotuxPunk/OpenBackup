package com.vandendaelen.openbackup.common.threads;

import com.sun.istack.internal.Nullable;
import com.vandendaelen.openbackup.common.utils.BackupHelper;

import java.nio.file.Path;
import java.util.function.Supplier;

public class BackupThread extends Thread{
    private final Path gameDir;
    private final Path openBackupDir;
    private final String worldName;
    private final int maxFileToKeep;
    private final boolean client;
    private final Supplier<Runnable> callback;

    public BackupThread(Path gameDir, Path openBackupDir, String worldName, int maxFileToKeep, boolean client, @Nullable Supplier<Runnable> callback) {
        super("OpenBackup_Backup");
        this.gameDir = gameDir;
        this.openBackupDir = openBackupDir;
        this.worldName = worldName;
        this.maxFileToKeep = maxFileToKeep;
        this.client = client;
        this.callback = callback;
    }

    @Override
    public void run() {
        BackupHelper.createBackup(this.openBackupDir, this.gameDir, this.worldName, this.maxFileToKeep, client);

        if (callback != null) {
            callback.get().run();
        }
    }
}
