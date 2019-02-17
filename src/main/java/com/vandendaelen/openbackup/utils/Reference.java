package com.vandendaelen.openbackup.utils;

public class Reference {
    public static final String MODID = "openbackup";
    public static final String MOD_NAME = "OpenBackup";
    public static final String DEP = "required-after:forge@[25.0.17,)";
    public static final String UPDATE_JSON = "https://raw.githubusercontent.com/LotuxPunk/OpenBackup/master/update.json";

    public static class VERSION{
        public static final String MCVERSION = "1.13.2";
        public static final String MAJORMOD = "0";
        public static final String MAJORAPI = "0";
        public static final String MINOR = "0";
        public static final String PATCH = "1";
        public static final String VERSION = MCVERSION+"-"+MAJORMOD+"."+MAJORAPI+"."+MINOR+"."+PATCH;
    }
}
