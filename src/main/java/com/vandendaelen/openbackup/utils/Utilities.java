package com.vandendaelen.openbackup.utils;

import net.minecraft.server.MinecraftServer;

public class Utilities {
    public static void enableWorldsSaving(MinecraftServer server, boolean flag){
        server.getWorlds().forEach((worldServer) -> worldServer.disableLevelSaving = !flag);
    }
}
