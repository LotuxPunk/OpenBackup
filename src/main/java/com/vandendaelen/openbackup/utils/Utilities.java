package com.vandendaelen.openbackup.utils;

import net.minecraft.server.MinecraftServer;

public class Utilities {
    public static void enableWorldsSaving(MinecraftServer server, boolean flag){
        for (int i = 0; i < server.worlds.length; i++) {
            server.worlds[i].disableLevelSaving = !flag;
        }
    }
}
