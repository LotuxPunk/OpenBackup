package com.vandendaelen.openbackup.handlers;

import com.vandendaelen.openbackup.OpenBackup;
import com.vandendaelen.openbackup.config.OBConfig;
import com.vandendaelen.openbackup.utils.Reference;
import com.vandendaelen.openbackup.utils.Utilities;
import com.vandendaelen.openbackup.utils.ZipUtils;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipOutputStream;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class OpenBackupServerEventHandler {

    public static long ticksUntilTheBackup = 0;
    public static long countDown;
    public static String DIR_PATH = "";

    @SubscribeEvent
    public static void onServerTickEvent(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START){
            countDown++;
            if (countDown > ticksUntilTheBackup){
                String sourceFile = OBConfig.PROPERTIES.worldname;
                Utilities.enableWorldsSaving(FMLCommonHandler.instance().getMinecraftServerInstance(),false);
                try {
                    OpenBackup.logger.info(OBConfig.TEXT.msgBackupStarted);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                    Date date = new Date();

                    FileOutputStream fos = new FileOutputStream(DIR_PATH+"\\"+dateFormat.format(date)+".zip");
                    ZipOutputStream zipOut = new ZipOutputStream(fos);
                    File fileToZip = new File(sourceFile);

                    ZipUtils.zipFile(fileToZip, fileToZip.getName(), zipOut);
                    zipOut.close();
                    fos.close();
                    OpenBackup.logger.info(OBConfig.TEXT.msgBackupDone);
                }
                catch (Exception e){
                    OpenBackup.logger.info(e.getMessage());
                }
                countDown=0;
                Utilities.enableWorldsSaving(FMLCommonHandler.instance().getMinecraftServerInstance(),true);
            }
        }
    }


}
