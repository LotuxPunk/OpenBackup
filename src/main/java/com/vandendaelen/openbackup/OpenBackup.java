package com.vandendaelen.openbackup;

import com.vandendaelen.openbackup.commands.CommandOpenBackup;
import com.vandendaelen.openbackup.configs.OBConfig;
import com.vandendaelen.openbackup.handlers.OpenBackupServerEventHandler;
import com.vandendaelen.openbackup.utils.Reference;
import com.vandendaelen.openbackup.utils.Timer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(Reference.MODID)
public class OpenBackup {
    public static final Logger LOGGER = LogManager.getLogger();
    public static OpenBackup INSTANCE;

    public OpenBackup() {
        INSTANCE = this;

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, OBConfig.CONFIG_SPEC);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        CommandOpenBackup.register(event.getCommandDispatcher());
        Timer timer = new Timer(OBConfig.getTimer()*20*60);
        //Directory things
        File file = ServerLifecycleHooks.getCurrentServer().getDataDirectory();
        OpenBackupServerEventHandler.DIR_PATH = new File(file, "openbackups").getAbsolutePath();
        File dirFile = new File(OpenBackupServerEventHandler.DIR_PATH);
        if (!dirFile.exists()) {
            if (dirFile.mkdir()) {
                OpenBackup.LOGGER.info("Directory is created!");
            } else {
                OpenBackup.LOGGER.info("Failed to create directory!");
            }
        }
    }

    @SubscribeEvent
    public void onServerStarted(FMLServerStartedEvent event) {
        //WorldSave name
        if (OBConfig.getDynamicWorldName()){
            OpenBackupServerEventHandler.worldName += ServerLifecycleHooks.getCurrentServer().getFolderName();
        }
        else{
            OpenBackupServerEventHandler.worldName += OBConfig.getWorldName();
        }
        LOGGER.info("World name : "+OpenBackupServerEventHandler.worldName);

        //Backup loop
        if (OBConfig.getEnable() && OBConfig.getBackupOnStart())
            OpenBackupServerEventHandler.startBackup();
    }
}
