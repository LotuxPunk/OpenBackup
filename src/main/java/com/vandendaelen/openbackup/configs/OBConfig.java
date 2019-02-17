package com.vandendaelen.openbackup.configs;


import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class OBConfig {

    public static final ServerConfig SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;
    static {
        final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        SERVER_SPEC = specPair.getRight();
        SERVER = specPair.getLeft();
    }
    public static int timer= 30;
    public static String msgBackupStarted = "[OpenBackup] Server backup - Started";
    public static String msgBackupDone = "[OpenBackup] Server backup - Done";
    public static String msgUnzip = "[OpenBackup] The backup is unziped in openbackup/restore";
    public static boolean dynamicWorldName = true;
    public static String worldname = "world";
    public static int fileToKeep = 15;
    public static boolean enable = true;
    public static boolean backupOnStart = true;
    public static int maxSizeBackupFolder = 30720;
    public static int broadcast = 1;

    public static void load(){
        timer = SERVER.timer.get();
        msgBackupDone = SERVER.msgBackupDone.get();
        msgBackupStarted = SERVER.msgBackupStarted.get();
        msgUnzip = SERVER.msgUnzip.get();
        dynamicWorldName = SERVER.dynamicWorldName.get();
        worldname = SERVER.worldName.get();
        fileToKeep = SERVER.filetokeep.get();
        enable = SERVER.enable.get();
        backupOnStart = SERVER.backupOnStart.get();
        maxSizeBackupFolder = SERVER.maxSizeBackupFolder.get();
        broadcast = SERVER.broadcast.get();
    }

    public static class ServerConfig{
        public ForgeConfigSpec.IntValue timer;
        public ForgeConfigSpec.ConfigValue<String> msgBackupStarted;
        public ForgeConfigSpec.ConfigValue<String> msgBackupDone;
        public ForgeConfigSpec.ConfigValue<String> msgUnzip;
        public ForgeConfigSpec.BooleanValue dynamicWorldName;
        public ForgeConfigSpec.ConfigValue<String> worldName;
        public ForgeConfigSpec.IntValue filetokeep;
        public ForgeConfigSpec.BooleanValue enable;
        public ForgeConfigSpec.BooleanValue backupOnStart;
        public ForgeConfigSpec.IntValue maxSizeBackupFolder;
        public ForgeConfigSpec.IntValue broadcast;

        public ServerConfig(ForgeConfigSpec.Builder builder) {
            builder.push("general");
            timer = builder
                    .comment("In minute")
                    .translation("config.timer.backup")
                    .defineInRange("timer",30,1,Integer.MAX_VALUE);
            dynamicWorldName = builder
                    .comment("Dynamicaly detect the world name ?")
                    .translation("config.prop.worldname.dynamic")
                    .define("dynamic",true);
            worldName = builder
                    .comment("If dynamicWorldName == false, select the world name")
                    .translation("config.prop.worldname")
                    .define("worldname","world");
            filetokeep = builder
                    .comment("Number files to keep")
                    .translation("config.prop.filetokeep")
                    .defineInRange("fileToKeep",15, 1,Integer.MAX_VALUE);
            enable = builder
                    .comment("Enable auto-backup")
                    .translation("config.prop.enable")
                    .define("enable", true);
            backupOnStart = builder
                    .comment("Enable backup at the server start")
                    .translation("config.prop.backup_start")
                    .define("backupOnStart", true);
            maxSizeBackupFolder = builder
                    .comment("Max weight of the openbackup folder in MB")
                    .translation("config.prop.sizetokeep")
                    .defineInRange("maxSizeBackupFolder",30720, 1024, Integer.MAX_VALUE);
            builder.pop();
            builder.push("messages");
            msgBackupStarted = builder
                    .translation("config.msg.started")
                    .define("msgBackupStarted", "[OpenBackup] Server backup - Started");
            msgBackupDone = builder
                    .translation("config.msg.done")
                    .define("msgBackupDone","[OpenBackup] Server backup - Done");
            msgUnzip = builder
                    .translation("config.msg.unzip")
                    .define("msgUnzip", "[OpenBackup] The backup is unziped in openbackups/restore");
            broadcast = builder
                    .comment("Enable the message's broadcast when the backup start/done. 1 - ALL, 2 - OP, 3- NONE")
                    .translation("config.prop.broadcast")
                    .defineInRange("broadcast",1,1,3);
            builder.pop();
        }
    }
}
