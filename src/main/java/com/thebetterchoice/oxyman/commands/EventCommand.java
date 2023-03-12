package com.thebetterchoice.oxyman.commands;

import com.thebetterchoice.oxyman.OxyPlugin;
import com.thebetterchoice.oxyman.specialevents.SpecialEvent;
import io.lumine.mythic.lib.MythicLib;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class EventCommand implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 2){
            String sus = args[0];
            String handlerConfigPath = args[1];
        }
        if (args.length == 1){
            String eventNameHope = args[0];
            if (OxyPlugin.getEventsConfig().getConfig().getKeys(false).contains(eventNameHope + "event")) {
                OxyPlugin.getMarket().getEventHandler().addEvent(new SpecialEvent(eventNameHope));;
            }
        }

        if (OxyPlugin.getMarket().getCurrentEvent() == null) {
            sender.sendMessage(MythicLib.plugin.parseColors( OxyPlugin.getPrefix() + " &7There is no event occurring."));
            return true;
        } else if (OxyPlugin.getMarket().getEventReSetter() == null) {
            sender.sendMessage(MythicLib.plugin.parseColors( OxyPlugin.getPrefix() + " &cError Occurred Please Contact Admin!"));
            return true;
        } else {
            String time = "";
            time = OxyPlugin.getMarket().getEventReSetter().getTimeLeft();
            String var10002 = OxyPlugin.getPrefix();
            sender.sendMessage(MythicLib.plugin.parseColors(var10002 + " &7An event is currently active; it will end in " + time + " - Check &c/sell"));
            return true;
        }
    }
}
