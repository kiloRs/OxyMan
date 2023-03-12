package com.thebetterchoice.oxyman.commands;

import com.thebetterchoice.oxyman.OxyPlugin;
import com.thebetterchoice.oxyman.specialevents.SpecialEvent;
import io.lumine.mythic.lib.MythicLib;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class TriggerEventCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2) {
            sender.sendMessage(MythicLib.plugin.parseColors("&4Invalid Command Format"));
            return false;
        } else {
            try {
                int index = Integer.parseInt(args[0]);
                int time = Integer.parseInt(args[1]);
                if (index < OxyPlugin.getMarket().getEventHandler().getEvents().size() && index >= 0) {
                    SpecialEvent e = OxyPlugin.getMarket().getEventHandler().getEvents().get(index);
                    if (e == null){
                        sender.sendMessage(MythicLib.plugin.parseColors("&cEvent not found!"));
                        return false;
                    }

                    boolean permission = sender.hasPermission("oxy.admin.event.override");
                    OxyPlugin.getMarket().setCurrentEvent(e, time, sender.isOp()|| permission);
                    sender.sendMessage(MythicLib.plugin.parseColors("&cEvent Starting " + e.getPath().toUpperCase(Locale.ROOT)));
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "Invalid Event Index, Event not found.");
                    return true;
                }
            } catch (Exception var7) {
                return false;
            }
        }
    }

}
