package com.thebetterchoice.oxyman.utils;

import com.thebetterchoice.oxyman.OxyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQL {

    public static void saveDemand(){

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Connection connection = null;
                    try {
                        connection = DriverManager.getConnection("jdbc:mysql://" + ConfigReference.MySQLIP + "/" + ConfigReference.MySQLDatabase, ConfigReference.MySQLUser, ConfigReference.MySQLPassword);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    if (connection == null) {
                        throw new RuntimeException("MySQL Not Found or Something");
                    }

                    final Statement statement = connection.createStatement();
                    statement.execute("CREATE TABLE IF NOT EXISTS `oxy_drugs` ( `name` VARCHAR(32) NOT NULL , `amountOfItems` INT(16) NULL , PRIMARY KEY (`name`))");
                    statement.close();

//                    for (Map.Entry<RegisteredDrug, Double> entry : DrugRegistry.getDemand().entrySet()) {
//                        RegisteredDrug registeredDrug = entry.getKey();
//                        Double savedDemand = entry.getValue();
//                        BasicDrug d = registeredDrug.getDrug();
//                        PreparedStatement prestate = connection.prepareStatement("INSERT INTO `oxy_drugs` VALUES (?, ?) ON DUPLICATE KEY UPDATE name=?, amountOfItems=?");
//                        prestate.setString(1, d.getName());
//                        prestate.setInt(2, Integer.parseInt(savedDemand + ""));
//                        prestate.setString(3, d.getName());
//                        prestate.setInt(4, Integer.parseInt(savedDemand + ""));
//                        prestate.execute();
//                        prestate.close();
//                    }
                    OxyPlugin.debug("Finished MySQL Saving and Closing Connection!");

                    connection.close();
                } catch (SQLException var6) {
                    Bukkit.getLogger().warning("--------------------------------------");
                    Bukkit.getLogger().warning("------------SQL Sever Error(2)-----------");
                    Bukkit.getLogger().warning(var6.getMessage());
                    Bukkit.getLogger().warning("--------------------------------------");
                }

            }
        }.runTaskAsynchronously(OxyPlugin.getPlugin());
    }
}
