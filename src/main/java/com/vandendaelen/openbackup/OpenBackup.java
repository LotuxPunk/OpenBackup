package com.vandendaelen.openbackup;

import com.vandendaelen.openbackup.command.CommandBackup;
import com.vandendaelen.openbackup.config.OBConfig;
import com.vandendaelen.openbackup.handlers.OpenBackupServerEventHandler;
import com.vandendaelen.openbackup.string.OBStrings;
import com.vandendaelen.openbackup.utils.Reference;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Mod(modid = Reference.MODID, name = Reference.MOD_NAME, version = Reference.VERSION.VERSION, dependencies = Reference.DEP,acceptableRemoteVersions = "*")
public class OpenBackup
{
    public static Logger LOGGER;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        //Permissions
        PermissionAPI.registerNode(OBStrings.Permission.permCmdBackup, DefaultPermissionLevel.OP, "Allows /openbackup commands");
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandBackup());

        //Directory things
        File file = FMLCommonHandler.instance().getMinecraftServerInstance().getDataDirectory();
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

    @EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        //World name
        if (OBConfig.PROPERTIES.dynamicWorldName){
            OpenBackupServerEventHandler.worldName += FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0).getWorldInfo().getWorldName();
        }
        else{
            OpenBackupServerEventHandler.worldName += OBConfig.PROPERTIES.worldname;
        }
        LOGGER.info("World name : "+OpenBackupServerEventHandler.worldName);

        //Backup loop
        if (OBConfig.PROPERTIES.enable)
            OpenBackupServerEventHandler.executorService.scheduleWithFixedDelay(OpenBackupServerEventHandler::startBackup,OBConfig.PROPERTIES.backupOnStart ? 1 : OBConfig.TIMER.timer,OBConfig.TIMER.timer, TimeUnit.MINUTES);
    }
}
