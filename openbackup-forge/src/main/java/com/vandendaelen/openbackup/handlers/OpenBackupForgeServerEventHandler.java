package com.vandendaelen.openbackup.handlers;

import com.vandendaelen.openbackup.common.utils.Reference;
import com.vandendaelen.openbackup.common.utils.Timer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class OpenBackupForgeServerEventHandler {
    public static boolean isRunning = false;
    public static String DIR_PATH = "";
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
