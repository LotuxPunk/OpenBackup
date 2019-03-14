package com.vandendaelen.openbackup.utils;

import net.minecraft.server.MinecraftServer;

public class Utilities {
    public static void enableWorldsSaving(MinecraftServer server, boolean flag){
        server.func_212370_w().forEach((worldServer) -> {
            worldServer.disableLevelSaving = !flag;
        });
    }
}
