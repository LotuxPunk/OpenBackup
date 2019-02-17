package com.vandendaelen.openbackup.threads;

import com.vandendaelen.openbackup.OpenBackup;
import com.vandendaelen.openbackup.helpers.FileHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.io.File;

public class ThreadDelete extends Thread {
    private File fileToDelete;
    //private Entity sender;
    private boolean isDeleted;
    private MinecraftServer server;

    public ThreadDelete(File file, MinecraftServer server) {
        super("OpenBackup_Delete");
        this.fileToDelete = file;
        this.server = server;
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
//            if (sender instanceof EntityPlayer){
//                sender.sendMessage(new TextComponentString(message));
//            }
        }
    }
}
