package com.vandendaelen.openbackup.helpers;

import com.vandendaelen.openbackup.OpenBackup;
import com.vandendaelen.openbackup.config.OBConfig;
import com.vandendaelen.openbackup.handlers.OpenBackupServerEventHandler;
import com.vandendaelen.openbackup.utils.ZipUtils;

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

public class BackupHelper {

    public static void deleteOldBackups(File backupDir){
        Path dirPath = backupDir.toPath();
        List<Path> files = new ArrayList<>();

        long maxFolderSize = (long) OBConfig.PROPERTIES.maxSizeBackupFolder * 1024 * 1024;
        double folderSize = 0;

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
            for(Path p : stream) {
                if(!p.getFileName().toString().equals("restore")) {
                    files.add(p);
                    folderSize += p.toFile().length();
                }

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

        while (files.size() > OBConfig.PROPERTIES.fileToKeep || folderSize > maxFolderSize) {
            Path path = files.get(0);
            files.remove(path);
            folderSize -= path.toFile().length();
            path.toFile().delete();
        }
    }

    public static void createBackup(String path, String sourceFile){
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            Date date = new Date();

            FileOutputStream fos = new FileOutputStream(path+File.separatorChar+ OpenBackupServerEventHandler.worldName +"-"+dateFormat.format(date)+".zip");
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            File fileToZip = new File(sourceFile);

            ZipUtils.zipFile(fileToZip, fileToZip.getName(), zipOut);
            zipOut.close();
            fos.close();
        }
        catch (Exception e){
            OpenBackup.logger.info(e.getMessage());
        }

        File backupDir = new File(path);
        deleteOldBackups(backupDir);
    }
}