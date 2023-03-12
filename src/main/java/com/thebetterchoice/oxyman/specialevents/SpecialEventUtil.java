package com.thebetterchoice.oxyman.specialevents;

import com.thebetterchoice.oxyman.OxyPlugin;
import com.thebetterchoice.oxyman.utils.ConfigFile;

import javax.annotation.Nullable;
import java.util.List;

public class SpecialEventUtil {

    private static ConfigFile configFile = OxyPlugin.getEventsConfig();

    public SpecialEventUtil(@Nullable ConfigFile config){
        if (config == null){
            return;
        }
        SpecialEventUtil.configFile = config;

    }

    public static Double getModifier(String id){
        return configFile.getConfig().getDouble( id + ".modifier",0.1);
    }
    public static int getWeight(String id){
        return configFile.getConfig().getInt(id + ".weight",1);
    }
    public static double getChance(String id){
        return configFile.getConfig().getDouble( id + ".chance",0.2);
    }
    public static List<String> getEffected(String id){
        return configFile.getConfig().getStringList( id + ".effects");
    }


    public static String getDisplay(String e) {
        return !configFile.getConfig().isString( e + ".display")?"Event Happened Now [Describe]":configFile.getConfig().getString( e + ".display");
    }
}
