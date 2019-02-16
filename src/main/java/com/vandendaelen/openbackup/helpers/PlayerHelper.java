package com.vandendaelen.openbackup.helpers;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Arrays;
import java.util.List;

public class PlayerHelper {

    public static void sendMessageToEveryone(String message){
        List<EntityPlayerMP> players = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
        for (EntityPlayerMP player:players) {
            player.sendMessage(new TextComponentString(message));
        }
    }

    public static void sendMessageToAdmins(String message){
        String[] admins = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getOppedPlayerNames();
        List<EntityPlayerMP> players = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
        for (EntityPlayerMP player:players) {
            if (Arrays.asList(admins).contains(player.getDisplayNameString()))
                player.sendMessage(new TextComponentString(message));
        }
    }
}
