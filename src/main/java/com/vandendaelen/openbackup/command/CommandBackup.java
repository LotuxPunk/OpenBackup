package com.vandendaelen.openbackup.command;

import com.vandendaelen.openbackup.handlers.OpenBackupServerEventHandler;
import com.vandendaelen.openbackup.helpers.FileHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

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
        return "/openbackup [backup, restore]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1)
            throw new CommandException("Error : " + getUsage(sender));

        if (args[0].equals("backup")){
            if (!OpenBackupServerEventHandler.isRunning)
                OpenBackupServerEventHandler.startBackupThread();
            else
                sender.sendMessage(new TextComponentString("Backup already running"));
        }
        else {
            if (args[0].equals("restore")){
                if (!args[1].isEmpty()){
                    OpenBackupServerEventHandler.restoreBackup(args[1], sender.getCommandSenderEntity());
                }
                else{
                    throw new CommandException("Error : you must specify a filename");
                }
            }
            else {
                throw new CommandException("Error : " + getUsage(sender));
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if(args.length < 2 ) {
            return getListOfStringsMatchingLastWord(args, "backup", "restore");
        }

        if (args[0].equals("restore")){
            return getListOfStringsMatchingLastWord(args, FileHelper.getFilesBackupDir());
        }

        return Collections.emptyList();
    }
}
