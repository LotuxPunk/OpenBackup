package com.vandendaelen.openbackup.config;

import com.vandendaelen.openbackup.utils.EnumBroadcast;
import com.vandendaelen.openbackup.utils.Reference;
import net.minecraftforge.common.config.Config;

@Config(modid = Reference.MODID,name = Reference.MOD_NAME)
public class OBConfig {

    public static final Timer TIMER = new Timer();
    public static final Text TEXT = new Text();
    public static final Properties PROPERTIES = new Properties();

    public static class Timer{
        @Config.LangKey("config.timer.backup")
        @Config.Comment("In minute")
        public int timer= 30;
    }

    public static class Text{
        @Config.LangKey("config.msg.started")
        public String msgBackupStarted = "[OpenBackup] Server backup - Started";
        @Config.LangKey("config.msg.done")
        public String msgBackupDone = "[OpenBackup] Server backup - Done";
        @Config.LangKey("config.msg.unzip")
        public String msgUnzip = "[OpenBackup] The backup is unziped in openbackup/restore";
    }

    public static class Properties{
        @Config.Comment("Dynamicaly detect the world name ?")
        @Config.LangKey("config.prop.worldname.dynamic")
        public boolean dynamicWorldName = true;

        @Config.Comment("If dynamicWorldName == false, select the world name")
        @Config.LangKey("config.prop.worldname")
        public String worldname = "world";

        @Config.LangKey("config.prop.filetokeep")
        @Config.Comment("Number files to keep")
        public int fileToKeep = 15;

        @Config.LangKey("config.prop.enable")
        @Config.Comment("Enable auto-backup")
        public boolean enable = true;

        @Config.LangKey("config.prop.backup_start")
        @Config.Comment("Enable backup at the server start")
        public boolean backupOnStart = true;

        @Config.LangKey("config.prop.sizetokeep")
        @Config.Comment("Max weight of the openbackup folder in MB")
        public int maxSizeBackupFolder = 30720;

        @Config.LangKey("config.prop.broadcast")
        @Config.Comment("Enable the message's broadcast when the backup start/done")
        public EnumBroadcast broadcast = EnumBroadcast.ALL;
    }
}
