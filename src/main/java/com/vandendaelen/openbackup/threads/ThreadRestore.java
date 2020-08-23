package com.vandendaelen.openbackup.threads;

import com.vandendaelen.openbackup.OpenBackup;
import com.vandendaelen.openbackup.configs.OBConfig;
import com.vandendaelen.openbackup.utils.ZipUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;

import java.io.File;

import static com.vandendaelen.openbackup.handlers.OpenBackupServerEventHandler.GAME_DIR;
import static com.vandendaelen.openbackup.handlers.OpenBackupServerEventHandler.OPENBACKUP_DIR;

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
        ZipUtils.unzip(OPENBACKUP_DIR + File.separatorChar + fileName, GAME_DIR+ File.separatorChar + "restore");
        server.runAsync(new PostActionRestore());
    }

    private class PostActionRestore implements Runnable{
        @Override
        public void run() {
            if (sender instanceof ServerPlayerEntity)
                sender.sendMessage(new StringTextComponent(OBConfig.getMsgUnzip()), sender.getUniqueID());
            OpenBackup.LOGGER.info(OBConfig.getMsgUnzip());
        }
    }
}
