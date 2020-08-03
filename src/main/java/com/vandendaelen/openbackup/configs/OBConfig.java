package com.vandendaelen.openbackup.configs;


import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class OBConfig {

    public static final OBConfig CONFIG;
    public static final ForgeConfigSpec CONFIG_SPEC;

    public final ForgeConfigSpec.IntValue timer;
    public final ForgeConfigSpec.ConfigValue<String> msgBackupStarted;
    public final ForgeConfigSpec.ConfigValue<String> msgBackupDone;
    public final ForgeConfigSpec.ConfigValue<String> msgUnzip;
    public final ForgeConfigSpec.BooleanValue dynamicWorldName;
    public final ForgeConfigSpec.ConfigValue<String> worldName;
    public final ForgeConfigSpec.IntValue filetokeep;
    public final ForgeConfigSpec.BooleanValue enable;
    public final ForgeConfigSpec.BooleanValue backupOnStart;
    public final ForgeConfigSpec.IntValue maxSizeBackupFolder;
    public final ForgeConfigSpec.IntValue broadcast;

    static {
        Pair<OBConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(OBConfig::new);
        CONFIG_SPEC = specPair.getRight();
        CONFIG = specPair.getLeft();
    }

    public OBConfig(ForgeConfigSpec.Builder builder) {
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

    public static int getTimer() {
        return CONFIG.timer.get();
    }

    public static String getMsgBackupStarted() {
        return CONFIG.msgBackupStarted.get();
    }

    public static String getMsgBackupDone() {
        return CONFIG.msgBackupDone.get();
    }

    public static String getMsgUnzip() {
        return CONFIG.msgUnzip.get();
    }

    public static boolean getDynamicWorldName() {
        return CONFIG.dynamicWorldName.get();
    }

    public static String getWorldName() {
        return CONFIG.worldName.get();
    }

    public static int getFiletokeep() {
        return CONFIG.filetokeep.get();
    }

    public static boolean getEnable() {
        return CONFIG.enable.get();
    }

    public static boolean getBackupOnStart() {
        return CONFIG.backupOnStart.get();
    }

    public static int getMaxSizeBackupFolder() {
        return CONFIG.maxSizeBackupFolder.get();
    }

    public static int getBroadcast() {
        return CONFIG.broadcast.get();
    }
}
