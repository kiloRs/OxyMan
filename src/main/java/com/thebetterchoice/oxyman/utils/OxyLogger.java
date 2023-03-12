package com.thebetterchoice.oxyman.utils;

import com.thebetterchoice.oxyman.OxyPlugin;
import com.thebetterchoice.oxyman.OxyUser;
import io.lumine.mythic.lib.MythicLib;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.UUID;
import java.util.logging.Level;

public class OxyLogger {


    public static void sendTo(OxyUser send, String mess){
        Player player = send.getPlayer();
        sendTo(player.getUniqueId(),mess);
    }
    public static void sendTo(UUID uuid, String mess){
        Player player = Bukkit.getPlayer(uuid);
        if (player == null){
            return;
        }

        sendTo(OxyUser.getStored(player),MythicLib.plugin.parseColors("&l&a[" + player.getName() + "]&a " ) + mess);
    }
   //Personal Level

    public static void log(String i){
        send(i,Level.INFO,false);
    }
    public static void error(String i){
        send(i,Level.SEVERE,false);
    }
    public static void debug(String i){
        send(i,Level.INFO,true);
    }
    public static void warn(String i){
        send(i,Level.WARNING,false);
    }

    public static void sendError(Exception e){
        File file = new File(OxyPlugin.getPlugin().getDataFolder(), "log.txt");
        if (!file.isFile()){
            try {
                file.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        try {
            e.printStackTrace(new PrintStream(file));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private static void sendAsDebugOnly(String i){
        send(i,Level.INFO,true);
    }
    private static void sendNoDebugs(String i, Level l){
        send(i,l,false);
    }


    public void sendAs(boolean debug, String calling){
        send(calling,Level.INFO,debug);
    }
    private static void send(String i, Level level, boolean debugOnly){
        if (debugOnly && !OxyPlugin.isDebug()){
            return;
        }
        if (level==Level.SEVERE){
            Bukkit.getConsoleSender().sendMessage(MythicLib.plugin.parseColors("&l&aOxyPlugin&r&f-&4" + i));
        }
        else if (level==Level.WARNING){
            Bukkit.getConsoleSender().sendMessage(MythicLib.plugin.parseColors("&l&aOxyPlugin&r&f-&c" + i));
        }
        else if (level==Level.INFO){
            Bukkit.getConsoleSender().sendMessage(MythicLib.plugin.parseColors("&l&aOxyPlugin&r&f-&e" + i));
        }
        else if (level==Level.ALL){
            Bukkit.getConsoleSender().sendMessage(MythicLib.plugin.parseColors("&l&aOxyPlugin&r&f-&a" + i));
        }
    }

    public static void sendError(String s) {
        sendError(new RuntimeException(s));
        send(s,Level.SEVERE,false);
    }
}
