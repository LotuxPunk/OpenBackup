package com.vandendaelen.openbackup.utils;

import net.minecraft.server.MinecraftServer;

public class Utilities {
    public static void enableWorldsSaving(MinecraftServer server, boolean flag){
        server.worlds.forEach((dimensionType, worldServer) -> {
            worldServer.disableLevelSaving = !flag;
        });
    }
}
