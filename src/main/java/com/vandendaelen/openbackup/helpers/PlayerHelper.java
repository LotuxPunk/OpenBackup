package com.vandendaelen.openbackup.helpers;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.Arrays;
import java.util.List;

public class PlayerHelper {
    public static void sendMessageToEveryone(String message){
        List<ServerPlayerEntity> players = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers();
        for (ServerPlayerEntity player:players) {
            player.sendMessage(new StringTextComponent(message), player.getUniqueID());
        }
    }

    public static void sendMessageToAdmins(String message){
        String[] admins = ServerLifecycleHooks.getCurrentServer().getPlayerList().getOppedPlayerNames();
        List<ServerPlayerEntity> players = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers();
        for (ServerPlayerEntity player:players) {
            if (Arrays.asList(admins).contains(player.getDisplayName().getUnformattedComponentText()))
                player.sendMessage(new StringTextComponent(message), player.getUniqueID());
        }
    }
}
