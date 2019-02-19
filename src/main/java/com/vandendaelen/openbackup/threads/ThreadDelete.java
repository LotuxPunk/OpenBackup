package com.vandendaelen.openbackup.threads;

import com.vandendaelen.openbackup.OpenBackup;
import com.vandendaelen.openbackup.helpers.FileHelper;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.io.File;

public class ThreadDelete extends Thread {
    private File fileToDelete;
    private CommandSource sender;
    private boolean isDeleted;
    private MinecraftServer server;

    public ThreadDelete(File file, MinecraftServer server, CommandSource source) {
        super("OpenBackup_Delete");
        this.fileToDelete = file;
        this.server = server;
        this.sender = source;
        this.isDeleted = false;
    }

    @Override
    public void run() {
        try {
            OpenBackup.LOGGER.info(fileToDelete.getAbsolutePath());
            isDeleted = FileHelper.deleteFile(fileToDelete);
        } catch (Exception e) {
            OpenBackup.LOGGER.info(e.getStackTrace());
        }
        server.addScheduledTask(new PostActionDelete());
    }

    private class PostActionDelete implements Runnable{
        @Override
        public void run() {
            if (isDeleted)
                sendInfo("Backup deleted");
            else
                sendInfo("Error on deleting the backup");
        }

        private void sendInfo(String message){
            OpenBackup.LOGGER.info(message);
            sender.sendFeedback(new TextComponentString(message), true);
        }
    }
}
