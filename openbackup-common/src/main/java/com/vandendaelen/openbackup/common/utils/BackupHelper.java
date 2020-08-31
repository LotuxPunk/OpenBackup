package com.vandendaelen.openbackup.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipOutputStream;

public class BackupHelper {
    private static void deleteOldestBackups(File backupDir, int maxFiletoKeep){
        final Path dirPath = backupDir.toPath();
        final List<Path> files = getFileList(dirPath);

        Collections.sort(files, (o1, o2) -> {
            try {
                return Files.getLastModifiedTime(o1).compareTo(Files.getLastModifiedTime(o2));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        });

        while (files.size() > maxFiletoKeep) {
            Path path = files.get(0);
            files.remove(path);
            path.toFile().delete();
        }
    }

    public static void createBackup(Path openBackupDir, Path gameDir, String worldName, int maxFileToKeep, boolean client){
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            Date date = new Date();

            FileOutputStream fos = new FileOutputStream(openBackupDir.toString() + File.separatorChar + worldName.trim() +"-"+dateFormat.format(date)+".zip");
            ZipOutputStream zipOut = new ZipOutputStream(fos);

            File fileToZip = null;
            if (client){
                fileToZip = new File(gameDir.toString(), MessageFormat.format("saves\\{0}", worldName));
            }
            else{
                fileToZip = new File(gameDir.toString(), worldName);
            }

            ZipUtils.zipFile(fileToZip, fileToZip.getName(), zipOut);

            zipOut.close();
            fos.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        deleteOldestBackups(openBackupDir.toFile(), maxFileToKeep);
    }

    public static List<Path> getFileList(Path dirPath){
        List<Path> files = new ArrayList<>();

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
            for(Path p : stream) {
                if(!p.getFileName().toString().equals("restore")) {
                    files.add(p);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return files;
    }
}
