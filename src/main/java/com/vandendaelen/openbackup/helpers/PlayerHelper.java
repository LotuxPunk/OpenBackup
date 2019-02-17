package com.vandendaelen.openbackup.helpers;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.Arrays;
import java.util.List;

public class PlayerHelper {
    public static void sendMessageToEveryone(String message){
        List<EntityPlayerMP> players = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers();
        for (EntityPlayerMP player:players) {
            player.sendMessage(new TextComponentString(message));
        }
    }

    public static void sendMessageToAdmins(String message){
        String[] admins = ServerLifecycleHooks.getCurrentServer().getPlayerList().getOppedPlayerNames();
        List<EntityPlayerMP> players = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers();
        for (EntityPlayerMP player:players) {
            if (Arrays.asList(admins).contains(player.getDisplayName().getUnformattedComponentText()))
                player.sendMessage(new TextComponentString(message));
        }
    }
}
