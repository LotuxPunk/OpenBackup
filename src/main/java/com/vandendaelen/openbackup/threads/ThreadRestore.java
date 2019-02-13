package com.vandendaelen.openbackup.threads;

import com.vandendaelen.openbackup.OpenBackup;
import com.vandendaelen.openbackup.config.OBConfig;
import com.vandendaelen.openbackup.handlers.OpenBackupServerEventHandler;
import com.vandendaelen.openbackup.utils.ZipUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.io.File;

import static com.vandendaelen.openbackup.handlers.OpenBackupServerEventHandler.DIR_PATH;

public class ThreadRestore extends Thread {
    private Entity sender;
    private String fileName;
    private MinecraftServer server;

    public ThreadRestore(Entity sender, String fileName, MinecraftServer server) {
        super("OpenBackup_Restore");
        this.sender = sender;
        this.fileName = fileName;
        this.server = server;
    }

    @Override
    public void run() {
        ZipUtils.unzip(DIR_PATH+ File.separatorChar+fileName, DIR_PATH+File.separatorChar+"restore");
        server.addScheduledTask(new PostActionRestore());
    }

    private class PostActionRestore implements Runnable{
        @Override
        public void run() {
            if (sender instanceof EntityPlayerMP)
                sender.sendMessage(new TextComponentString(OBConfig.TEXT.msgUnzip));
            OpenBackup.LOGGER.info(OBConfig.TEXT.msgUnzip);
            OpenBackupServerEventHandler.isRunning = false;
        }
    }
}
