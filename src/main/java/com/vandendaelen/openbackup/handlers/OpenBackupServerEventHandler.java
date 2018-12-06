package com.vandendaelen.openbackup.handlers;

import com.vandendaelen.openbackup.OpenBackup;
import com.vandendaelen.openbackup.config.OBConfig;
import com.vandendaelen.openbackup.helpers.BackupHelper;
import com.vandendaelen.openbackup.helpers.PlayerHelper;
import com.vandendaelen.openbackup.utils.Reference;
import com.vandendaelen.openbackup.utils.Utilities;
import com.vandendaelen.openbackup.utils.ZipUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.File;

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
                PlayerHelper.sendMessageToEveryone(OBConfig.TEXT.msgBackupStarted);
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
                BackupHelper.createBackup(DIR_PATH, OpenBackupServerEventHandler.worldName);
                FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        Utilities.enableWorldsSaving(FMLCommonHandler.instance().getMinecraftServerInstance(),true);
                        OpenBackup.logger.info(OBConfig.TEXT.msgBackupDone);
                        PlayerHelper.sendMessageToEveryone(OBConfig.TEXT.msgBackupDone);
                        countDown = 0;
                        isRunning = false;
                    }
                });
            }
        };
        thread.start();
    }

    public static void restoreBackup(String fileName, Entity sender){
        Thread thread1 = new Thread("WorldRestoreThread"){
            @Override
            public void run() {
                ZipUtils.unzip(DIR_PATH+File.separatorChar+fileName, DIR_PATH+File.separatorChar+"restore");
                FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        if (sender instanceof EntityPlayerMP)
                            sender.sendMessage(new TextComponentString(OBConfig.TEXT.msgUnzip));
                        OpenBackup.logger.info(OBConfig.TEXT.msgUnzip);
                    }
                });
            }
        };
        thread1.start();
    }
}
