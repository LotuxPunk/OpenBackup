package com.vandendaelen.openbackup.helpers;

import com.vandendaelen.openbackup.OpenBackup;
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
}
