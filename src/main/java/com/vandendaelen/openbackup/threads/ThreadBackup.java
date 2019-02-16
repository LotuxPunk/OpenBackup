package com.vandendaelen.openbackup.threads;

import com.vandendaelen.openbackup.OpenBackup;
import com.vandendaelen.openbackup.config.OBConfig;
import com.vandendaelen.openbackup.handlers.OpenBackupServerEventHandler;
import com.vandendaelen.openbackup.helpers.BackupHelper;
import com.vandendaelen.openbackup.helpers.PlayerHelper;
import com.vandendaelen.openbackup.utils.Utilities;
import net.minecraft.server.MinecraftServer;

import static com.vandendaelen.openbackup.handlers.OpenBackupServerEventHandler.DIR_PATH;

public class ThreadBackup extends Thread {
    private MinecraftServer server;
    public ThreadBackup(MinecraftServer server) {
        super("OpenBackup_Backup");
        this.server = server;
    }

    @Override
    public void run() {
        BackupHelper.createBackup(DIR_PATH, OpenBackupServerEventHandler.worldName);
        server.addScheduledTask(new PostBackupAction());
    }

    private class PostBackupAction implements Runnable{
        @Override
        public void run() {
            Utilities.enableWorldsSaving(server ,true);
            OpenBackup.LOGGER.info(OBConfig.TEXT.msgBackupDone);
            PlayerHelper.sendMessageToEveryone(OBConfig.TEXT.msgBackupDone);
            OpenBackupServerEventHandler.isRunning = false;
        }
    }
}
