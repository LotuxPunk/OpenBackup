package com.vandendaelen.openbackup.handlers;

import com.vandendaelen.openbackup.OpenBackup;
import com.vandendaelen.openbackup.config.OBConfig;
import com.vandendaelen.openbackup.utils.Reference;
import com.vandendaelen.openbackup.utils.Utilities;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class OpenBackupServerEventHandler {

    public static long ticksUntilTheBackup = 0;
    public static long countDown;
    public static String DIR_PATH = "";

    @SubscribeEvent
    public static void onServerTickEvent(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && OBConfig.PROPERTIES.enable){
            countDown++;
            if (countDown > ticksUntilTheBackup){
                OpenBackup.logger.info(OBConfig.TEXT.msgBackupStarted);
                Thread thread = new Thread("WorldBackupThread"){
                    @Override
                    public void run() {
                        Utilities.createBackup(DIR_PATH, OBConfig.PROPERTIES.worldname);
                    }
                };
                thread.run();
                countDown = 0;
                OpenBackup.logger.info(OBConfig.TEXT.msgBackupDone);
            }
        }
    }


}
