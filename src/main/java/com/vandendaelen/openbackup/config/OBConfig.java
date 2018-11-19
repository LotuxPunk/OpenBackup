package com.vandendaelen.openbackup.config;

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
        public String msgBackupStarted = "Server backup - Started";
        @Config.LangKey("config.msg.stopped")
        public String msgBackupStopped = "Server backup - Stopped";
        @Config.LangKey("config.msg.done")
        public String msgBackupDone = "Server backup - Done";
    }

    public static class Properties{
        @Config.LangKey("config.prop.worldname")
        public String worldname = "world";
    }
}
