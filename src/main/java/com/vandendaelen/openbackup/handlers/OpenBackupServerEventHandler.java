package com.vandendaelen.openbackup.handlers;

import com.vandendaelen.openbackup.OpenBackup;
import com.vandendaelen.openbackup.configs.OBConfig;
import com.vandendaelen.openbackup.helpers.PlayerHelper;
import com.vandendaelen.openbackup.threads.ThreadBackup;
import com.vandendaelen.openbackup.threads.ThreadRestore;
import com.vandendaelen.openbackup.utils.EnumBroadcast;
import com.vandendaelen.openbackup.utils.Reference;
import com.vandendaelen.openbackup.utils.Timer;
import com.vandendaelen.openbackup.utils.Utilities;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class OpenBackupServerEventHandler {
    public static boolean isRunning = false;
    public static String GAME_DIR = "";
    public static String OPENBACKUP_DIR = "";
    public static String worldName = "";

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event){
        if (event.phase == TickEvent.Phase.START){
            if (!isRunning && Timer.getInstance().update()){
                startBackup();
            }
        }
    }

    public static void startBackup(){
        isRunning = true;
        OpenBackup.LOGGER.info(OBConfig.getMsgBackupStarted());
        if (OBConfig.getBroadcast() == EnumBroadcast.ALL.getValue())
            PlayerHelper.sendMessageToEveryone(OBConfig.getMsgBackupStarted());
        if (OBConfig.getBroadcast() == EnumBroadcast.OP.getValue())
            PlayerHelper.sendMessageToAdmins(OBConfig.getMsgBackupStarted());

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
