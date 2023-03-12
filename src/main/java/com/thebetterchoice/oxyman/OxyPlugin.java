package com.thebetterchoice.oxyman;


import com.thebetterchoice.oxyman.commands.*;
import com.thebetterchoice.oxyman.drugs.BasicDrug;
import com.thebetterchoice.oxyman.drugs.DrugRegistry;
import com.thebetterchoice.oxyman.drugs.RegisteredDrug;
import com.thebetterchoice.oxyman.listener.UserListener;
import com.thebetterchoice.oxyman.menu.SellManager;
import com.thebetterchoice.oxyman.menu.ShopGUI;
import com.thebetterchoice.oxyman.menu.bonuses.BonusManager;
import com.thebetterchoice.oxyman.sales.GlobalBonusHandler;
import com.thebetterchoice.oxyman.sales.Market;
import com.thebetterchoice.oxyman.sales.PlayerBonusHandler;
import com.thebetterchoice.oxyman.specialevents.SpecialEventHandler;
import com.thebetterchoice.oxyman.utils.*;
import io.lumine.mythic.lib.MythicLib;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class OxyPlugin extends JavaPlugin {
    /**
     * Main Player Boost Handler
     */
    public static Map<UUID, PlayerBonusHandler> playerBoosts;

    private static Economy economy;
    /**
     * Used Market [Only 1 Per Server/World]
     */
    private static Market market;
    @Getter
    private static Plugin plugin;
    private static Logger logger;
    /**
     * Attempt to get all values of drugs registered [amount]
     */
    @Getter
    private static int totalDrugsRegistered;

    @Getter
    private static ConfigFile eventsConfig = null;
    @Getter
    private static ConfigFile navigationConfig = null;
    @Getter
    private static ConfigFile multipliers = null;
    @Getter
    private static ConfigFile messagesConfig = null;
    @Getter
    private static ConfigFile drugsConfiguration = null;
    private static NavigationReference navigationReference = null;
    private static ConfigReference configReference = null;
    @Getter
    private static MessageConstants constants;
    private static GlobalBonusHandler globalBonusHandler;
    private static PermissionLoader permissionLoader;
    private static Connection connection;

    @Getter
    private static SellManager sellManager;


    public static FileConfiguration getFileConfiguration() {
        return plugin.getConfig();
    }

    public static String getPrefix() {
        return MessageConstants.get().getPREFIX();
    }

    public static Market getMarket() {
        return market;
    }

    public static void setMarket(Market market) {
        OxyPlugin.market = market;
    }

    public static GlobalBonusHandler getGlobalBonus() {
        return globalBonusHandler;
    }



    public static Economy getEconomy() {
        return economy;
    }

    public static void throwError(String message) {
        RuntimeException runtimeException = new RuntimeException(message);
        runtimeException.printStackTrace();
    }

    public static PermissionLoader getPermissionLoader() {
        return permissionLoader;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static boolean isDebug() {
        return getFileConfiguration().contains("Debug.Enabled")&&getFileConfiguration().getBoolean("Debug.Enabled");
    }

    public static void explain(String loaded, boolean staging) {
        if (staging) {
            Bukkit.getConsoleSender().sendMessage(MythicLib.plugin.parseColors("&l&b" + getPrefix() + " &a" + loaded));
        }
        else{
            Bukkit.getConsoleSender().sendMessage(MythicLib.plugin.parseColors("&l&c" + getPrefix() + " &4" + loaded));
        }
    }

    public static  Map<Inventory,Boolean> getInventories() {
        HashMap<Inventory, Boolean> i = new HashMap<>();
        if (i.isEmpty()){
            OxyLogger.error("No Saved Inventory!");
        }

        return i;
    }

    /**
     * @return The main bonus manager for the server!
     */
    public static BonusManager getBonusManager() {
        return new BonusManager();
    }


    @Override
    public void onLoad() {
        plugin = this;
        logger = Logger.getLogger("Minecraft");
        loadConfigFiles();
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + ConfigReference.MySQLIP + "/" + ConfigReference.MySQLDatabase, ConfigReference.MySQLUser, ConfigReference.MySQLPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (connection != null){
            OxyPlugin.debug("MySQL Obtained!");
        }
        else {
            OxyLogger.sendError("No SQL Connection Loaded!");
        }


        playerBoosts = new HashMap<>();


        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Oxy " + ChatColor.DARK_AQUA + "-" + ChatColor.RESET + "Loaded!");


        registerConfig();



    }


    private void loadConfigFiles() {
        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();
        eventsConfig = new ConfigFile(this,"/","events");
        navigationConfig = new ConfigFile(this,"/","navigation");
        messagesConfig = new ConfigFile(this,"/","messages");
        drugsConfiguration = new ConfigFile(this,"/","drugs");
        multipliers = new ConfigFile(this,"/","multipliers");
        MessageConstants.get();

        Stats stats = new Stats();

        stats.load();

        if (navigationReference == null){
            navigationReference = new NavigationReference();
        }
        if (configReference == null){
            configReference = new ConfigReference();
        }
    }



    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            OxyPlugin.log("Plugin Vault not found.");
            return false;
        }

        Economy eco = new VaultSupport().getEconomy();

        economy = eco;

        return eco != null;
    }


    @Override
    public void onEnable() {
        plugin = this;
        new Stats();


        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + ConfigReference.MySQLIP + "/" + ConfigReference.MySQLDatabase, ConfigReference.MySQLUser, ConfigReference.MySQLPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (connection == null) {
            throw new RuntimeException("No MYSQL Connection Found");
        }
        else {
            log("MySQL has been connected!");
        }

        if (setupEconomy()) {
            OxyPlugin.log("Hooked to Vault Now");
        }
        else {
            if (economy != null){
                OxyPlugin.log("Eco Found!");
                return;
            }
            throwError("No Vault Connection Possible or Attained!");
        }

        if (economy == null){
            OxyLogger.sendError("No Economy Loaded!");
        }
        registerCommands();
        permissionLoader = new PermissionLoader();
        playerBoosts = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(permissionLoader,this);
        Bukkit.getPluginManager().registerEvents(new UserListener(), this);
        Bukkit.getPluginManager().registerEvents(new ShopGUI.ShopGUIListener(),this);
        constants = MessageConstants.get();
        globalBonusHandler = new GlobalBonusHandler();
        DrugRegistry drugRegistry = new DrugRegistry(this);
        drugRegistry.postLoad();
        Market m = new Market(this);
        setMarket(m);

        SpecialEventHandler eventHandler = m.getEventHandler();

        if (eventHandler.getEvents().isEmpty()) {

            OxyLogger.sendError("No Custom Special Events Loaded!");
        }


        DrugRegistry.loadItems();
        Map<String, RegisteredDrug> registeredDrugs = DrugRegistry.getRegisteredDrugs();
        totalDrugsRegistered = registeredDrugs.size();




        debug("Successfully Loaded Plugin!");


    }




    @Override
    public void onDisable() {

        SQL.saveDemand();

    }

    private void registerConfig() {
        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();
    }

    private void registerCommands() {
        this.getCommand("oxysell").setExecutor(new SellCommand());
        this.getCommand("oxysellall").setExecutor(new SellAllCommand());
        this.getCommand("oxy").setExecutor(new MainCommand());
        this.getCommand("triggerevent").setExecutor(new TriggerEventCommand());
        this.getCommand("influence").setExecutor(new InfluenceCommand());
        this.getCommand("oxyevent").setExecutor(new EventCommand());
        this.getCommand("oxybonus").setExecutor(new BonusCommand());
        this.getCommand("oxycheck").setExecutor(new CheckItemCommand());
    }

    public static String format(double val){
        String format = getFileConfiguration().getString("Format.Standard", "#,###.00");
        DecimalFormat h = MythicLib.plugin.getMMOConfig().newDecimalFormat(format);
        String symbol = h.getCurrency().getSymbol();
        String formatted = h.format(val);
        return MessageConstants.get().getCURRENCY().replace("<currency>",symbol) + "" + formatted;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (label.equalsIgnoreCase("oxy")){
            return new MainCommand().onCommand(sender, command, label, args);
        }
        if (label.equalsIgnoreCase("oxyconfirm")){
            return new SellCommand().onCommand(sender, command, label, args);
        }
        if (label.equalsIgnoreCase("influence")){
            return new InfluenceCommand().onCommand(sender,command,label,args);
        }
        if (label.equalsIgnoreCase("oxyevent")){
            return new EventCommand().onCommand(sender, command, label, args);
        }
        if (label.equalsIgnoreCase("oxybonus")){
            return new BonusCommand().onCommand(sender, command, label, args);
        }
        if (label.equalsIgnoreCase("oxysell")){
            return new SellCommand().onCommand(sender, command, label, args);
        }
        if (label.equalsIgnoreCase("oxysellall")){
            return new SellAllCommand().onCommand(sender, command, label, args);
        }
        if (label.equalsIgnoreCase("triggerevent")){
            return new TriggerEventCommand().onCommand(sender, command, label, args);
        }
        if (label.equalsIgnoreCase("oxycheck")){
            return new CheckItemCommand().onCommand(sender, command, label, args);
        }
        return false;
    }
    public static void reload() {
        if (loadMySQL()){
            SQL.saveDemand();
        }
        File file = new File(OxyPlugin.getPlugin().getDataFolder(), "config.yml");
        YamlConfiguration.loadConfiguration(file);
        Bukkit.getScheduler().cancelTasks(OxyPlugin.getPlugin());
        globalBonusHandler = new GlobalBonusHandler();
        playerBoosts.clear();
        market = new Market(OxyPlugin.getPlugin());
        market.addDrugs(BasicDrug.parseDrug(OxyPlugin.getDrugsConfiguration().getConfig()));

        NavigationReference.loadItems(navigationConfig);

        DrugRegistry.addItems().runTask(OxyPlugin.getPlugin());
//        OxyPlugin.log("Connection Reset to MySQL");

    }

    private static boolean loadMySQL() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + ConfigReference.MySQLIP + "/" + ConfigReference.MySQLDatabase, ConfigReference.MySQLUser, ConfigReference.MySQLPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection != null;
    }

    public static void send(CommandSender commandSender, String i){
        commandSender.sendMessage(MythicLib.plugin.parseColors(MessageConstants.get().getPREFIX())+" "+  i);
    }
    public static void debug(String emergence){
        if (isDebug()){
            Bukkit.getConsoleSender().sendMessage(MythicLib.plugin.parseColors("&aOxy&e[DEBUG] " + "&b" + emergence ));
        }
    }
    public static void log(String log){
        logger.info("[ " + plugin.getName() + "Debug ]" + log);
    }

}
