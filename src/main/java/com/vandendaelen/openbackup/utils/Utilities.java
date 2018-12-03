package com.vandendaelen.openbackup.utils;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.DimensionManager;

import java.util.Arrays;
import java.util.List;

public class Utilities {

    public static void enableWorldsSaving(MinecraftServer server, boolean flag){
        for (int i = 0; i < server.worlds.length; i++) {
            server.worlds[i].disableLevelSaving = !flag;
        }
    }

    public static void unloadWorlds(){
        List<Integer> worldsID = Arrays.asList(DimensionManager.getIDs());
        for (Integer worldID: worldsID) {
            DimensionManager.unloadWorld(worldID.intValue());
        }
    }


}
