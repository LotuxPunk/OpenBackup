package com.vandendaelen.openbackup.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.vandendaelen.openbackup.handlers.OpenBackupServerEventHandler;
import com.vandendaelen.openbackup.helpers.BackupHelper;
import com.vandendaelen.openbackup.helpers.FileHelper;
import com.vandendaelen.openbackup.threads.ThreadDelete;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.impl.GiveCommand;
import net.minecraft.command.impl.WhitelistCommand;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.server.command.ForgeCommand;

import java.io.File;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.List;

public class CommandOpenBackup {
    public static void register(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(Commands.literal("openbackup")
            .requires(s->s.hasPermissionLevel(ServerLifecycleHooks.getCurrentServer().getOpPermissionLevel()))
                .then(Commands.literal("backup")
                        .executes(ctx ->backup(ctx.getSource())))
                .then(Commands.literal("restore")
                    .then(Commands.argument("file", StringArgumentType.string()).suggests((ctx, builder) -> ISuggestionProvider.suggest(FileHelper.getFilesBackupDir(),builder))
                        .executes(ctx -> restore(ctx.getSource(), StringArgumentType.getString(ctx,"file")))))
                .then(Commands.literal("status")
                    .executes(ctx -> status(ctx.getSource())))
                .then(Commands.literal("delete")
                    .then(Commands.argument("file", StringArgumentType.string()).suggests((ctx, builder) -> ISuggestionProvider.suggest(FileHelper.getFilesBackupDir(),builder))
                        .executes(ctx->delete(ctx.getSource(), StringArgumentType.getString(ctx,"file"))))));
    }

    private static int backup(CommandSource source){
        if (!OpenBackupServerEventHandler.isRunning) {
            source.sendFeedback(new TextComponentString("Backup started, check server's log for more info"), true);
            OpenBackupServerEventHandler.startBackup();
        }
        else
            source.sendErrorMessage(new TextComponentString("Backup already running"));
        return Command.SINGLE_SUCCESS;
    }

    private static int restore(CommandSource source, String filename){
        OpenBackupServerEventHandler.restoreBackup(filename, source.getEntity());
        return Command.SINGLE_SUCCESS;
    }

    private static int status(CommandSource source){
        List<Path> files = BackupHelper.getFileList(new File(OpenBackupServerEventHandler.DIR_PATH).toPath());
        long folderSize = BackupHelper.getSizeOfFileList(files);
        int nbFiles = files.size();
        source.sendFeedback(new TextComponentString(MessageFormat.format("{0} file(s), {1}MB -> ~{2}GB",nbFiles,folderSize/1024/1024,folderSize/1024/1024/1024)),true);
        return Command.SINGLE_SUCCESS;
    }

    private static int delete(CommandSource source, String filename) {
        File fileToDelete = new File(OpenBackupServerEventHandler.DIR_PATH + File.separatorChar + filename);
        ThreadDelete threadDelete = new ThreadDelete(fileToDelete, ServerLifecycleHooks.getCurrentServer(), source);
        threadDelete.start();
        return Command.SINGLE_SUCCESS;
    }
}
