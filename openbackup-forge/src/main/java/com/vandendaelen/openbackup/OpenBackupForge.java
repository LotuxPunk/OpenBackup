package com.vandendaelen.openbackup;

import com.vandendaelen.openbackup.common.backup.BackupManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(OpenBackupForge.MOD_ID)
public class OpenBackupForge {
    public static final String MOD_ID = "openbackup-forge";
    private static final Logger LOGGER = LogManager.getLogger();

    public OpenBackupForge() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarted(FMLServerStartedEvent event) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        BackupManager bm = BackupManager.getInstance();
        bm.init(
                15,
                30*60*20,
                server.getDataDirectory().getAbsoluteFile().toPath(),
                new File(server.getDataDirectory(), "openbackup").toPath(),
                server.func_240793_aU_().getWorldName(),
                FMLEnvironment.dist == Dist.CLIENT
            );

        //TODO : Check if enabled in config
        bm.createBackup();
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event){
        if (event.phase == TickEvent.Phase.START){
            BackupManager.getInstance().tick();
        }
    }
}
