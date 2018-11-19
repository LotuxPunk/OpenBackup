package com.vandendaelen.openbackup;

import com.vandendaelen.openbackup.config.OBConfig;
import com.vandendaelen.openbackup.handlers.OpenBackupServerEventHandler;
import com.vandendaelen.openbackup.utils.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.command.server.CommandSaveOn;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = Reference.MODID, name = Reference.MOD_NAME, version = Reference.VERSION.VERSION)
public class OpenBackup
{
    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();

    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        //Timer
        OpenBackupServerEventHandler.ticksUntilTheBackup = OBConfig.TIMER.timer*60*20;

        //Directory things
        File file = FMLCommonHandler.instance().getMinecraftServerInstance().getDataDirectory();
        OpenBackupServerEventHandler.DIR_PATH = file.getAbsolutePath().concat("openbackups").replace(".","");
        File dirFile = new File(OpenBackupServerEventHandler.DIR_PATH);
        if (!dirFile.exists()) {
            if (dirFile.mkdir()) {
                logger.info("Directory is created!");
            } else {
                logger.info("Failed to create directory!");
            }
        }



    }
}
