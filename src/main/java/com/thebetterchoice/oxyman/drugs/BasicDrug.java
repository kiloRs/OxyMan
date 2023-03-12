package com.thebetterchoice.oxyman.drugs;

import com.thebetterchoice.oxyman.OxyPlugin;
import com.thebetterchoice.oxyman.Stats;
import io.lumine.mythic.lib.MythicLib;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.LiveMMOItem;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class BasicDrug {
    private final String displayName;
    private final String name;
    private final DrugId drugId;
    private final double price;
    private final MMOItem mmoItem;
    private final ItemStack stack;


    public BasicDrug(Inventory inventory, int slot){
        this(inventory.getItem(slot));
    }
    public BasicDrug(ItemStack itemStack){
        this(MMOItems.getID(itemStack),itemStack);
    }

    public BasicDrug(String i, ItemStack itemStack) {
        this(new RegisteredDrug( i),itemStack);
    }

    public BasicDrug(RegisteredDrug registeredDrug, ItemStack itemStack) {
        if (itemStack == null || MMOItems.getID(itemStack) == null || MMOItems.getID(itemStack).isEmpty()){
            throw new RuntimeException("Bad Use of Basic Drug");
        }
        this.price = registeredDrug.getCurrentCost();
        this.name = registeredDrug.getPath();
        this.displayName = MythicLib.plugin.parseColors("&a" + StringUtils.capitalize(name));
        this.drugId = registeredDrug;
        this.mmoItem = new LiveMMOItem(itemStack);
        this.stack = itemStack;
    }

    public BasicDrug(DrugId drugId, ItemStack itemStack) {
        this(((RegisteredDrug) drugId),itemStack);
    }

    public ItemStack getDrugsStack(boolean forDisplay) {
        return getDrugAsMMOItem().newBuilder().build(forDisplay);
    }

    public MMOItem getDrugAsMMOItem() {
        return this.drugId.getItem().newBuilder().getMMOItem();
    }


    public static List<RegisteredDrug> parseDrug(ConfigurationSection section) {

        if (section.getKeys(false).size() == 0) {
            OxyPlugin.log("No Drugs Found in Configuration!");
            return new ArrayList<>();
        }

        MMOItem actual;
        List<RegisteredDrug> loadingDrugs = new ArrayList<>();
        String mmoitem = null;
        RegisteredDrug registeredDrug;

        for (String eachItemEntry : section.getKeys(false)) {
            if (!section.contains(eachItemEntry)) {
                continue;
            }
            mmoitem = eachItemEntry.toUpperCase(Locale.ROOT);

            String s;

            s = mmoitem;
            actual = MMOItems.plugin.getMMOItem(Stats.DRUG, mmoitem);
            if (actual == null) {
                OxyPlugin.log("No MMOItem: " + mmoitem);
                continue;
            }
            registeredDrug = new RegisteredDrug(actual.getId());

            if (registeredDrug == null){
                throw  new RuntimeException("InvaliD");
            }

            OxyPlugin.log("LoadingDrug Passed Loading from Configurations [" + mmoitem + "]");

            loadingDrugs.add(registeredDrug);

        }

        return loadingDrugs;
    }

//    private static void loadMySQL(List<LoadingDrug> loadingDrugs) {
//        try {
//            Connection connection = null;
//            try {
//                connection = OxyPlugin.getConnection();
//            }catch (Exception e){
//                e.printStackTrace();
//                OxyPlugin.log("Connection Error!");
//            }
//            if (connection == null){
//                throw new RuntimeException("Bad MySQL Connection!");
//            }
//            Statement statement = null;
//            try {
//                statement = connection.createStatement();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            try {
//                if (statement != null) {
//                    statement.execute("CREATE TABLE IF NOT EXISTS `oxy_drugs` ( `name` VARCHAR(32) NOT NULL , `amountOfItems` INT(16) NULL , PRIMARY KEY (`name`))");
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            try {
//                if (statement != null) {
//                    statement.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//            for (LoadingDrug loadingDrug : loadingDrugs) {
//                PreparedStatement prestate = null;
//                try {
//                    if (connection != null) {
//                        prestate = connection.prepareStatement("SELECT `amountOfItems` FROM `oxy_drugs` WHERE `name` = ? ");
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    if (prestate != null) {
//                        prestate.setString(1, loadingDrug.drugId.getPath());
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//                ResultSet res = null;
//                try {
//                    if (prestate != null) {
//                        res = prestate.executeQuery();
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    if (res != null && res.next()) {
//                        loadingDrug.setAmountOfItems(res.getInt(1));
//                        loadingDrug.setPrice(Math.max(loadingDrug.getPrice(), loadingDrug.getBasePrice()));
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//
//                try {
//                    if (prestate != null) {
//                        prestate.close();
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    if (res != null) {
//                        res.close();
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            if (connection != null) {
//                connection.close();
//            }
//        } catch (SQLException var15) {
//            Bukkit.getLogger().warning("--------------------------------------");
//            Bukkit.getLogger().warning("------------SQL Sever Error-----------");
//            Bukkit.getLogger().warning(var15.getMessage());
//            Bukkit.getLogger().warning("--------------------------------------");
//
//        }
//    }
//    private void loadSQL() {
//        try {
//            Connection connection = null;
//            try {
//                connection = OxyPlugin.getConnection();
//            }catch (Exception e){
//                e.printStackTrace();
//                OxyPlugin.log("Connection Error!");
//            }
//            if (connection == null){
//                throw new RuntimeException("Bad MySQL Connection!");
//            }
//            Statement statement = null;
//            try {
//                statement = connection.createStatement();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            try {
//                if (statement != null) {
//                    statement.execute("CREATE TABLE IF NOT EXISTS `oxy_drugs` ( `name` VARCHAR(32) NOT NULL , `amountOfItems` INT(16) NULL , PRIMARY KEY (`name`))");
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            try {
//                if (statement != null) {
//                    statement.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//                PreparedStatement prestate = null;
//                try {
//                    if (connection != null) {
//                        prestate = connection.prepareStatement("SELECT `amountOfItems` FROM `oxy_drugs` WHERE `name` = ? ");
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    if (prestate != null) {
//                        prestate.setString(1, this.drugId.getPath());
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//                ResultSet res = null;
//                try {
//                    if (prestate != null) {
//                        res = prestate.executeQuery();
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    if (res != null && res.next()) {
//                        this.setAmountOfItems(res.getInt(1));
//                        this.setPrice(Math.max(this.getPrice(), this.getBasePrice()));
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//
//                try {
//                    if (prestate != null) {
//                        prestate.close();
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    if (res != null) {
//                        res.close();
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//
//
//            if (connection != null) {
//                connection.close();
//            }
//        } catch (SQLException var15) {
//            Bukkit.getLogger().warning("--------------------------------------");
//            Bukkit.getLogger().warning("------------SQL Sever Error-----------");
//            Bukkit.getLogger().warning(var15.getMessage());
//            Bukkit.getLogger().warning("--------------------------------------");
//
//        }
//    }
//
    public double getPrice() {
        if (!DrugRegistry.hasDrug(this.drugId.getItem())){
            return drugId.getCurrentCost();
        }

        return Math.max(Stats.getPrice(DrugRegistry.getRegisteredDrugs().get(this.drugId.getItem().getId())), drugId.getCurrentCost());
    }

    public void setPrice(double price){
        this.setPrice(price,null);
    }
    public void setPrice(double price,@Nullable UUID storeKey) {
        RegisteredDrug d = DrugRegistry.getRegisteredDrugs().get(this.getDrugAsMMOItem().getId());
        Stats.setPrice(d,price,storeKey);
    }


    public String getDisplayName() {
        return this.displayName;
    }

    public String getName() {
        return this.name;
    }

    public DrugId getDrugId() {
        return drugId;
    }
}
