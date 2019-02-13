package com.vandendaelen.openbackup.command;

import com.vandendaelen.openbackup.OpenBackup;
import com.vandendaelen.openbackup.handlers.OpenBackupServerEventHandler;
import com.vandendaelen.openbackup.helpers.BackupHelper;
import com.vandendaelen.openbackup.helpers.FileHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandBackup extends CommandBase {

    List<String> subcommands = Arrays.asList("backup","restore","status", "delete");

    @Override
    public String getName() {
        return "openbackup";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        StringBuilder usageString = new StringBuilder();

        usageString.append("/openbackup ");
        usageString.append(subcommands.get(0));

        for (String sc : subcommands.subList(1,subcommands.size())) {
            usageString.append(MessageFormat.format(" | {0}", sc));
        }

        return usageString.toString();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1 && !subcommands.contains(args[0]))
            throw new CommandException("Error : " + getUsage(sender));

        switch (subcommands.indexOf(args[0])){
            case 0:{ //backup
                if (!OpenBackupServerEventHandler.isRunning)
                    OpenBackupServerEventHandler.startBackupThread();
                else
                    sender.sendMessage(new TextComponentString("Backup already running"));
                break;
            }
            case 1 :{ //restore
                if (!args[1].isEmpty()){
                    OpenBackupServerEventHandler.restoreBackup(args[1], sender.getCommandSenderEntity());
                }
                else
                    throw new CommandException("Error : you must specify a filename");
                break;
            }
            case 2:{ //status
                List<Path> files = BackupHelper.getFileList(new File(OpenBackupServerEventHandler.DIR_PATH).toPath());
                long folderSize = BackupHelper.getSizeOfFileList(files);
                int nbFiles = files.size();
                sender.sendMessage(new TextComponentString(MessageFormat.format("{0} file(s), {1}MB -> ~{2}GB",nbFiles,folderSize/1024/1024,folderSize/1024/1024/1024)));
                break;
            }
            case 3 : { //delete
                if (args.length > 1) {
                    try {
                        File fileToDelete = new File(OpenBackupServerEventHandler.DIR_PATH + File.separatorChar + args[1]);
                        OpenBackup.LOGGER.info(fileToDelete.getAbsolutePath());
                        if (FileHelper.deleteFile(fileToDelete))
                            sender.sendMessage(new TextComponentString("Backup deleted"));
                        else
                            sender.sendMessage(new TextComponentString("Error on deleting the backup"));
                    } catch (Exception e) {
                        sender.sendMessage(new TextComponentString(e.getMessage()));
                    }
                }
                break;
            }
            default:
                throw new CommandException("Error : " + getUsage(sender));
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if(args.length < 2 ) {
            return getListOfStringsMatchingLastWord(args, subcommands);
        }

        if (args[0].equals("restore") || args[0].equals("delete")){
            return getListOfStringsMatchingLastWord(args, FileHelper.getFilesBackupDir());
        }

        return Collections.emptyList();
    }
}
