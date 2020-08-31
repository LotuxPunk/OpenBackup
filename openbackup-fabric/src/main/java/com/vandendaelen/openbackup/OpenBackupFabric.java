package com.vandendaelen.openbackup;

import com.vandendaelen.openbackup.common.backup.BackupManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public class OpenBackupFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(minecraftServer -> {
            BackupManager.getInstance().init(
                    10,
                    30*60*20,
                    minecraftServer.getRunDirectory().toPath(),
                    new File(minecraftServer.getRunDirectory(), "openbackup").toPath(),
                    minecraftServer.getSaveProperties().getLevelName(),
                    FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT
            );

            BackupManager.getInstance().createBackup();
        });

        ServerTickEvents.START_SERVER_TICK.register(minecraftServer -> BackupManager.getInstance().tick());
    }
}
