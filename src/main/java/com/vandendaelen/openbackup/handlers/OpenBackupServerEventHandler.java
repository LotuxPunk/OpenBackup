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

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class OpenBackupServerEventHandler {

    public static boolean isRunning = false;
    public static String DIR_PATH = "";
    public static String worldName = "";
    public static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public static void startBackupThread(){
        isRunning = true;
        OpenBackup.LOGGER.info(OBConfig.TEXT.msgBackupStarted);
        PlayerHelper.sendMessageToEveryone(OBConfig.TEXT.msgBackupStarted);

        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        Utilities.enableWorldsSaving(server,false);

        Thread thread = new Thread("WorldBackupThread"){
            @Override
            public void run() {
                BackupHelper.createBackup(DIR_PATH, OpenBackupServerEventHandler.worldName);
                server.addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        Utilities.enableWorldsSaving(server ,true);
                        isRunning = false;
                        OpenBackup.LOGGER.info(OBConfig.TEXT.msgBackupDone);
                        PlayerHelper.sendMessageToEveryone(OBConfig.TEXT.msgBackupDone);
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
                ZipUtils.unzip(DIR_PATH+ File.separatorChar+fileName, DIR_PATH+File.separatorChar+"restore");
                FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        if (sender instanceof EntityPlayerMP)
                            sender.sendMessage(new TextComponentString(OBConfig.TEXT.msgUnzip));
                        OpenBackup.LOGGER.info(OBConfig.TEXT.msgUnzip);
                    }
                });
            }
        };
        thread1.start();
    }


}
