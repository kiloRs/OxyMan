package com.thebetterchoice.oxyman.utils;

import com.thebetterchoice.oxyman.OxyPlugin;
import com.thebetterchoice.oxyman.OxyUser;
import com.thebetterchoice.oxyman.PermissionInstance;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class ConfigReference {
    @Getter
    private static final Map<UUID, OxyUser> data = new HashMap<>();
    public static Integer defaultBoostTime;
    @Getter
    private static final List<PermissionInstance> permissions = new ArrayList<>();

    //Other
    @Getter
    private static final FileConfiguration section = OxyPlugin.getPlugin().getConfig();
    public static int maxActiveBonuses;
    public static boolean deepEnabled;




    public ConfigReference(){

        loadAll();
    }

    public void loadAll() {
        OxyPlugin.log("Starting Load All of Configurations!");
//        if (!section.isConfigurationSection("sql")){
//            section.set("sql.ip","localhost:3306");
//            section.set("sql.database","core");
//            section.set("sql.username","root");
//            section.set("sql.password","");
//        OxyPlugin.getConfigFile().save();
//        OxyPlugin.getPlugin().saveConfig();
//        }
        ConfigurationSection sql = section.getConfigurationSection("sql");
        if (sql == null){
            OxyPlugin.throwError("Bad SQL Section!");
            return;
        }
        MySQLIP=sql.getString("ip","localhost:3306");
        MySQLDatabase=sql.getString("database","core");
        MySQLUser=sql.getString("username","root");
        MySQLPassword= sql.getString("password","");

//        if (!section.contains("Boost.Time.Default")){
//            section.set("Boost.Time.Default",60);
//        }
        String boostTimeDefault = "Boost.Time.Default";
        defaultBoostTime = section.getInt(boostTimeDefault,60);


//        if (!section.contains("Garbage")){
//            ArrayList<String> s = new ArrayList<>();
//            s.add("WHEAT");
//            s.add("PUMPKIN");
//            section.set("garbage", s);
//            save();
//        }
        garbage = section.getStringList("Garbage");

        String path = "Server.Demand.Reduction.Length";
//        if (!section.contains(path)){
//            section.set(path,-1);
//            save();        }
        reductionLength = section.getInt(path,10);

        String fresh = "Server.Demand.Reduction.Repeat";
//        if (!section.contains(fresh)){
//            section.set(fresh,-1);
//            save();
//        }
        reductionRepeat = section.getInt(fresh,50);

        String hole = "Server.Demand.Repeat";
//        if (!section.contains(hole)){
//            section.set(hole,-1);
//            save();
//        }
        itemLoadRepeat = section.getInt(hole,40);
        String delay = "Server.Demand.Delay";
//        if (!section.contains(delay)){
//            section.set(delay,-1);
//            save();
//        }
        itemLoadStartLength = section.getInt(delay,1);

        String basPath = "Server.Demand.Base";

//        if (!section.contains(basPath)){
//            section.set(basPath,0);
//            OxyPlugin.getPlugin().save();
//
//        }
        serverBaseCost = OxyPlugin.getPlugin().getConfig().getDouble(basPath,0);

        String maxCost = "Server.Cost.Max";
//        if (!section.contains(maxCost)){
//            section.set(maxCost,-1);
//            OxyPlugin.getPlugin().save();
//        }
        serverMaxCost = OxyPlugin.getPlugin().getConfig().getDouble(maxCost,10000);

        String minCost = "Server.Cost.Min";
//        if (!section.contains(minCost)){
//            section.set(minCost,-1);
//            OxyPlugin.getPlugin().save();
//        }
        serverMinCost = OxyPlugin.getPlugin().getConfig().getDouble(minCost,0);

        String maxDemand = "Server.Demand.Max";
//        if (!section.contains(maxDemand)){
//            section.set(maxDemand,-1);
//            OxyPlugin.getPlugin().save();
//        }
        serverDemandMax = OxyPlugin.getPlugin().getConfig().getDouble(maxDemand,10000);

        String minDemand = "Server.Demand.Min";
//        if (!section.contains(minDemand)){
//            section.set(minDemand,-1);
//            OxyPlugin.getPlugin().save();
//        }
        serverDemandMin = OxyPlugin.getPlugin().getConfig().getDouble(minDemand,0);

        String baseDemand = "Server.Demand.Base";
//        if (!section.contains(baseDemand)){
//            section.set(baseDemand,-1);
//            OxyPlugin.getPlugin().save();
//        }
        serverDemandBase = OxyPlugin.getPlugin().getConfig().getDouble(baseDemand,0);
//
        String oop = "Server.Demand.Offset";
//        if (!section.contains(oop)){
//            section.set(oop,-1);
//            OxyPlugin.getPlugin().save();
//        }
        serverDemandOffset = OxyPlugin.getPlugin().getConfig().getDouble(oop,1);
        String car = "Load.Timer";
//        if (!section.contains(car)){
//            section.set(car,1);
//            OxyPlugin.getPlugin().save();
//        }
        itemLoadTimer = OxyPlugin.getPlugin().getConfig().getLong(car,10);

        demandChance = OxyPlugin.getPlugin().getConfig().getDouble("Server.Demand.Chance",1.0);
        if (OxyPlugin.getMultipliers().getConfig().getKeys(false).isEmpty()){
            return;
        }
        maxActiveBonuses = OxyPlugin.getPlugin().getConfig().getInt("Special.Bonuses.Max");
        String pathDeep = "Server.Influence.Deep.";
        deepEnabled = OxyPlugin.getFileConfiguration().getBoolean(pathDeep +  "Enabled",false);

        loadPermissions();
        OxyPlugin.debug("Loaded All Configuration Settings -> !  ! ! ! ! !!  ! ! !! !");
    }

    public static void loadPermissions() {
        for (String eachKey : OxyPlugin.getMultipliers().getConfig().getKeys(false)) {
            if (eachKey.isEmpty()){
                continue;
            }
            permissions.add(new PermissionInstance(eachKey));
            OxyPlugin.debug("Loaded Permission: " + eachKey);
        }
        OxyPlugin.debug("Permissions have been loaded!");
    }

    public static List<String> garbage;

    public static String MySQLIP = section.getString("sql.ip","localhost:3306");
    public static String MySQLDatabase = section.getString("sql.database","core");
    public static String MySQLUser = section.getString("sql.user","root");
    public static String MySQLPassword = section.getString("sql.password","");
    public static int reductionLength;
    public static int reductionRepeat;
    public static int itemLoadRepeat;
    public static int itemLoadStartLength;
    public static double serverBaseCost;
    public static double serverMaxCost;


    public static double serverMinCost;
    public static double serverDemandMax;
    public static double serverDemandMin;
    public static double serverDemandBase;
    public static double demandChance;

    public static double serverDemandOffset;

    public static long itemLoadTimer = 1;

}
