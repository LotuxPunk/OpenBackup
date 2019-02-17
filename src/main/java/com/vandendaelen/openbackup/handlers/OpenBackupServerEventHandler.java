package com.vandendaelen.openbackup.handlers;

import com.vandendaelen.openbackup.OpenBackup;
import com.vandendaelen.openbackup.configs.OBConfig;
import com.vandendaelen.openbackup.helpers.PlayerHelper;
import com.vandendaelen.openbackup.threads.ThreadBackup;
import com.vandendaelen.openbackup.threads.ThreadRestore;
import com.vandendaelen.openbackup.utils.EnumBroadcast;
import com.vandendaelen.openbackup.utils.Utilities;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class OpenBackupServerEventHandler {
    public static boolean isRunning = false;
    public static String DIR_PATH = "";
    public static String worldName = "";
    public static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public static void startBackup(){
        isRunning = true;
        OpenBackup.LOGGER.info(OBConfig.msgBackupStarted);
        if (OBConfig.broadcast == EnumBroadcast.ALL.getValue())
            PlayerHelper.sendMessageToEveryone(OBConfig.msgBackupStarted);
        if (OBConfig.broadcast == EnumBroadcast.OP.getValue())
            PlayerHelper.sendMessageToAdmins(OBConfig.msgBackupStarted);

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        Utilities.enableWorldsSaving(server,false);

        ThreadBackup threadBackup = new ThreadBackup(server);
        threadBackup.start();
    }

    public static void restoreBackup(String fileName, Entity sender){
        ThreadRestore threadRestore = new ThreadRestore(sender, fileName, ServerLifecycleHooks.getCurrentServer());
        threadRestore.start();
    }
}
