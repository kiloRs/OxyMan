package com.thebetterchoice.oxyman.commands;

import com.thebetterchoice.oxyman.OxyPlugin;
import com.thebetterchoice.oxyman.sales.Bonus;
import com.thebetterchoice.oxyman.sales.PlayerBonusHandler;
import com.thebetterchoice.oxyman.utils.MessageConstants;
import io.lumine.mythic.lib.MythicLib;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BonusCommand implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command");
            return true;
        } else {
            PlayerBonusHandler pbh = null;
            if (OxyPlugin.playerBoosts.containsKey(player.getUniqueId())){
                pbh = OxyPlugin.playerBoosts.get(player.getUniqueId());
            }
            if (args[0].equalsIgnoreCase("amount")){
                if (pbh == null){
                    player.sendRawMessage(MythicLib.plugin.parseColors(MessageConstants.get().getBONUS_TOTAL_ALL().replace("%amountOfItems%",1 + "")));
                    return true;
                }
                List<Bonus> totalList = pbh.getAllBonusesOfPlayer();int total = totalList.size();

                total  = total + 1;
                player.sendRawMessage(MessageConstants.get().getBONUS_TOTAL_ALL().replace("%amountOfBonuses%",total + ""));
            }


            int gbonus = (int)(OxyPlugin.getGlobalBonus().getCurrentBonus() * 100.0D);
            int pbonus = 0;
            String gtimeleft = "";
            if (OxyPlugin.getGlobalBonus().getFormattedTime() != null) {
                gtimeleft = OxyPlugin.getGlobalBonus().getFormattedTime();
            }

            if (OxyPlugin.playerBoosts.containsKey(player.getUniqueId())) {
                pbonus = (int)(((OxyPlugin.playerBoosts.get(player.getUniqueId())).getCurrentBonus() * 100.0D));
            }

            String var10001;
            String prefix = OxyPlugin.getPrefix();
            if (gbonus == 0) {
                sender.sendMessage(prefix + MessageConstants.get().getGLOBAL_BONUS_EMPTY());
            } else {
                sender.sendMessage(MythicLib.plugin.parseColors(prefix + MessageConstants.get().getGLOBAL_BONUS_TOTAL().replace("%bonus%",gbonus + "").replace("%formatted_time%",gtimeleft)));
            }

            if (pbonus == 0) {
                var10001 = prefix;
                sender.sendMessage(MessageConstants.get().getPERSONAL_BONUS_EMPTY());
            } else {
                var10001 = prefix;
                sender.sendMessage(MessageConstants.get().getPREFIX() + MessageConstants.get().getBONUS_TOTAL_SPECIFIC().replace("%bonus%",pbonus + ""));


                for (PlayerBonusHandler.BonusCloser be : pbh.closerMap.values()) {
                    var10001 =prefix;
                    sender.sendMessage(var10001 + MessageConstants.get().getPERSONAL_BONUS_END().replace("%bonus%",be.getOxyBonus().getAmount() + "").replace("%formatted_time%",be.getFormattedTime()));

                }
            }

            return true;
        }
    }
}
