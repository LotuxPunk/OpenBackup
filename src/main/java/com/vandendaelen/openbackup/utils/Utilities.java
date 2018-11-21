package com.vandendaelen.openbackup.utils;

import com.vandendaelen.openbackup.OpenBackup;
import com.vandendaelen.openbackup.config.OBConfig;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipOutputStream;

public class Utilities {

    public static void enableWorldsSaving(MinecraftServer server, boolean flag){
        for (int i = 0; i < server.worlds.length; i++) {
            server.worlds[i].disableLevelSaving = !flag;
        }
    }

    public static void deleteOldBackups(Path dirPath){
        List<Path> files = new ArrayList<>();
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
            for(Path p : stream) {
                files.add(p);
            }
        }
        catch (Exception e){
            OpenBackup.logger.info(e.getMessage());
        }

        Collections.sort(files, (o1, o2) -> {
            try {
                return Files.getLastModifiedTime(o1).compareTo(Files.getLastModifiedTime(o2));
            } catch (IOException e) {
                OpenBackup.logger.info(e.getMessage());
            }
            return 0;
        });

        while (files.size() > OBConfig.PROPERTIES.fileToKeep) {
            Path path = files.get(0);
            files.remove(path);
            path.toFile().delete();
        }
    }

    public static void createBackup(String path, String sourceFile){
        Utilities.enableWorldsSaving(FMLCommonHandler.instance().getMinecraftServerInstance(),false);
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            Date date = new Date();

            FileOutputStream fos = new FileOutputStream(path+"\\"+dateFormat.format(date)+".zip");
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            File fileToZip = new File(sourceFile);

            ZipUtils.zipFile(fileToZip, fileToZip.getName(), zipOut);
            zipOut.close();
            fos.close();
        }
        catch (Exception e){
            OpenBackup.logger.info(e.getMessage());
        }
        Utilities.enableWorldsSaving(FMLCommonHandler.instance().getMinecraftServerInstance(),true);

        File backupDir = new File(path);
        if (backupDir.list().length > OBConfig.PROPERTIES.fileToKeep){
            Utilities.deleteOldBackups(backupDir.toPath());
        }
    }
}
