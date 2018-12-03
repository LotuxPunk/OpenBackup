package com.vandendaelen.openbackup.handlers;

import com.vandendaelen.openbackup.OpenBackup;
import com.vandendaelen.openbackup.config.OBConfig;
import com.vandendaelen.openbackup.utils.Reference;
import com.vandendaelen.openbackup.utils.Utilities;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class OpenBackupServerEventHandler {

    public static long ticksUntilTheBackup = 0;
    public static long countDown;
    public static boolean isRunning = false;
    public static String DIR_PATH = "";
    public static String worldName = "";

    @SubscribeEvent
    public static void onServerTickEvent(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && OBConfig.PROPERTIES.enable && !isRunning){
            countDown++;
            if (countDown > ticksUntilTheBackup){
                OpenBackup.logger.info(OBConfig.TEXT.msgBackupStarted);
                MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
                Utilities.enableWorldsSaving(server,false);
                startBackupThread();
            }
        }
    }

    public static void startBackupThread(){
        isRunning = true;
        Thread thread = new Thread("WorldBackupThread"){
            @Override
            public void run() {
                Utilities.createBackup(DIR_PATH, OpenBackupServerEventHandler.worldName);
                FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        Utilities.enableWorldsSaving(FMLCommonHandler.instance().getMinecraftServerInstance(),true);
                        OpenBackup.logger.info(OBConfig.TEXT.msgBackupDone);
                        countDown = 0;
                        isRunning = false;
                    }
                });
            }
        };
        thread.start();
    }
}
