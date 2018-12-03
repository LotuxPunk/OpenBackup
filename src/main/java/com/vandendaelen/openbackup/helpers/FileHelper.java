package com.vandendaelen.openbackup.helpers;

import com.vandendaelen.openbackup.OpenBackup;
import com.vandendaelen.openbackup.handlers.OpenBackupServerEventHandler;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class FileHelper {

    public static void deleteWorldForlder(File worldFolder){
        try{
            FileUtils.deleteDirectory(worldFolder);
        }
        catch (Exception e) {
            OpenBackup.logger.info(e.getMessage());
        }
    }

    public static String[] getFilesBackupDir(){
        return new File(OpenBackupServerEventHandler.DIR_PATH).list();
    }
}
