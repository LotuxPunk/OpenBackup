package com.vandendaelen.openbackup.command;

import com.vandendaelen.openbackup.OpenBackup;
import com.vandendaelen.openbackup.config.OBConfig;
import com.vandendaelen.openbackup.handlers.OpenBackupServerEventHandler;
import com.vandendaelen.openbackup.helpers.PlayerHelper;
import com.vandendaelen.openbackup.utils.Utilities;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandBackup extends CommandBase {
    @Override
    public String getName() {
        return "openbackup";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/openbackup [backup]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1)
            throw new CommandException("Error : " + getUsage(sender));

        if (args[0].equals("backup")){
            if (!OpenBackupServerEventHandler.isRunning){
                OpenBackup.logger.info(OBConfig.TEXT.msgBackupStarted);
                PlayerHelper.sendMessageToEveryone(OBConfig.TEXT.msgBackupStarted);
                OpenBackupServerEventHandler.startBackupThread();
            }
        }
        else {
            throw new CommandException("Error : " + getUsage(sender));
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if(args.length < 2 ) {
            return getListOfStringsMatchingLastWord(args, "backup");
        }

        return Collections.emptyList();
    }
}
