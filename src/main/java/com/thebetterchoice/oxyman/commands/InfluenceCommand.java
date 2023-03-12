package com.thebetterchoice.oxyman.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class InfluenceCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length <= 2) {
            return false;
        } else {}
        return false;
    }
}
//            BasicDrug target = null;
////
////            for (RegisteredDrug drug : OxyPlugin.getMarket().getDrugs()) {
////                if (args[0].equalsIgnoreCase(drug.getPath())) {
////                    target = drug.getDrug();
////                }
////            }
//
//            if (target == null) {
//                sender.sendMessage(MessageFormat.format("{0}LoadingDrug not found", ChatColor.RED));
//                return false;
//            } else {
//
//                double amount;
//                try {
//                    amount = Double.parseDouble(args[1]);
//                } catch (Exception var9) {
//                    return false;
//                }
//                OxyPlugin.send(sender, MessageConstants.get().getINFLUENCE_COMMAND().replace("%drug%", target.getDisplayName()).replace("%base%", target.getDemand() - amount + "").replace("%modified%", target.getDemand() + ""));
//                return true;
//            }
//        }

