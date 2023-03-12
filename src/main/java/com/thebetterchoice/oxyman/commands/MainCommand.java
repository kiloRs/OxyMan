package com.thebetterchoice.oxyman.commands;

import com.jeff_media.jefflib.NumberUtils;
import com.thebetterchoice.oxyman.OxyPlugin;
import com.thebetterchoice.oxyman.sales.Bonus;
import com.thebetterchoice.oxyman.sales.PlayerBonusHandler;
import com.thebetterchoice.oxyman.utils.ConfigReference;
import com.thebetterchoice.oxyman.utils.MessageConstants;
import io.lumine.mythic.lib.MythicLib;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return false;
        } else if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("oxy.admin.reload")) {
                OxyPlugin.reload();
                OxyPlugin.getPermissionLoader().reload();
                sender.sendMessage(MythicLib.plugin.parseColors((MessageConstants.get().getRELOAD())));
                return true;
            } else {
                sender.sendMessage(MythicLib.plugin.parseColors(("&4You can not use this command without permissions!")));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("reset")) {
            if (sender.hasPermission("oxy.admin.reset")) {
                OxyPlugin.getMarket().drugReset();
                sender.sendMessage(ChatColor.GREEN + "Sales Reset.");
                return true;
            } else {
                sender.sendMessage(MythicLib.plugin.parseColors( "&cYou do not have permission to use this command!"));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("setbonus")) {
            if (!sender.hasPermission("oxy.admin.setbonus")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                return true;
            }
            if (args.length == 2 && NumberUtils.isInteger(args[1])){
                double boostamount = (double)Integer.parseInt(args[1]) / 100.0D;
                String first = args[0];
                int time = ConfigReference.defaultBoostTime>0?ConfigReference.defaultBoostTime:60;
                boolean isGlobal = first.equalsIgnoreCase("all");
                if (boostamount == 0.0D) {
                    if (isGlobal) {
                        OxyPlugin.getGlobalBonus().reset();
                    } else {
                        Player p = Bukkit.getPlayer(first);
                        if (p == null) {
                            sender.sendMessage(MythicLib.plugin.parseColors("&cPlayer Not Found."));
                            return true;
                        }

                        if (OxyPlugin.playerBoosts.containsKey(p.getUniqueId())) {
                            PlayerBonusHandler playerBonusHandler = OxyPlugin.playerBoosts.get(p.getUniqueId());

                            double total = playerBonusHandler.getCurrentBonus();

                            OxyPlugin.debug("Player Bonus of " + total );
                        }
                    }

                    sender.sendMessage(MythicLib.plugin.parseColors("&aBonus was reset!"));
                    return true;
                } else if (boostamount > 0.0D) {
                    if (boostamount > 1.0D) {
                        sender.sendMessage(MythicLib.plugin.parseColors("&eBoosts can only go up to 100%"));
                        return true;
                    } else {
                        UUID uuid = null;
                        Bonus boost = new Bonus(boostamount, time);
                        if (isGlobal) {
                            sender.sendMessage(MythicLib.plugin.parseColors("&aBoost Added!"));
                        } else {
                            Player p = Bukkit.getPlayer(first);
                            if (p == null) {
                                sender.sendMessage(MythicLib.plugin.parseColors("&aNo Player found with that name!"));
                                return true;
                            }

                            uuid = p.getUniqueId();
                            if (!(sender instanceof Player) || !((Player)sender).getUniqueId().equals(p.getUniqueId())) {
                                sender.sendMessage(MythicLib.plugin.parseColors(MessageConstants.get().getBONUS_START().replace("%bonus%",boostamount +"").replace("%formatted_time%",boost.getFormattedTime())));
                            }
                        }

                        if (isGlobal) {
                            OxyPlugin.getGlobalBonus().addBoost(new Bonus(boostamount, time));
                        } else {
                            if (!OxyPlugin.playerBoosts.containsKey(uuid)) {
                                OxyPlugin.playerBoosts.put(uuid, new PlayerBonusHandler(uuid));
                            }

                            PlayerBonusHandler bh = OxyPlugin.playerBoosts.get(uuid);
                            bh.addBonus(new Bonus(boostamount, time));
                        }
                    }

                    return true;
                }
            }
            if (args.length >= 4 && NumberUtils.isInteger(args[2]) && NumberUtils.isInteger(args[3])) {
                int time = Integer.parseInt(args[2]);
                double boostamount = (double)Integer.parseInt(args[3]) / 100.0D;
                String first = args[1];
                boolean isGlobal = first.equalsIgnoreCase("all");
                if (boostamount == 0.0D) {
                    if (isGlobal) {
                        OxyPlugin.getGlobalBonus().reset();
                    } else {
                        Player p = Bukkit.getPlayer(first);
                        if (p == null) {
                            sender.sendMessage(MythicLib.plugin.parseColors("&cPlayer Not Found."));
                            return true;
                        }

                        if (OxyPlugin.playerBoosts.containsKey(p.getUniqueId())) {
                            PlayerBonusHandler playerBonusHandler = OxyPlugin.playerBoosts.get(p.getUniqueId());

                            double total = playerBonusHandler.getCurrentBonus();

                            OxyPlugin.debug("Player Bonus of " + total );
                        }
                    }

                    sender.sendMessage(MythicLib.plugin.parseColors(MessageConstants.get().getBONUS_RESET().replace("%bonus%",MythicLib.plugin.getMMOConfig().newDecimalFormat("#,###.0").format(boostamount))));
                    return true;
                } else if (boostamount >= 0.0D && time >= 0) {
                    if (boostamount > 1.0D) {
                        sender.sendMessage(MythicLib.plugin.parseColors("&eBoosts can only go up to 100%"));
                        return true;
                    } else {
                        UUID uuid = null;
                        Bonus boost = new Bonus(boostamount, time);
                        if (isGlobal) {
                            sender.sendMessage(MythicLib.plugin.parseColors("&aBoost Added!"));
                        } else {
                            Player p = Bukkit.getPlayer(first);
                            if (p == null) {
                                sender.sendMessage(MythicLib.plugin.parseColors("&aNo Player found with that name!"));
                                return true;
                            }

                            uuid = p.getUniqueId();
                            if (!(sender instanceof Player) || !((Player)sender).getUniqueId().equals(p.getUniqueId())) {
                                sender.sendMessage(MythicLib.plugin.parseColors(MessageConstants.get().getBONUS_START().replace("%bonus%",boostamount +"").replace("%formatted_time%",boost.getFormattedTime())));
                            }
                        }

                        if (isGlobal) {
                            OxyPlugin.getGlobalBonus().addBoost(new Bonus(boostamount, time));
                        } else {
                            if (!OxyPlugin.playerBoosts.containsKey(uuid)) {
                               OxyPlugin.playerBoosts.put(uuid, new PlayerBonusHandler(uuid));
                            }

                            PlayerBonusHandler bh = OxyPlugin.playerBoosts.get(uuid);
                            bh.addBonus(new Bonus(boostamount, time));
                        }
                    }

                    return true;
                } else {
                    sender.sendMessage(MythicLib.plugin.parseColors("&aOxy SetBonus") +"&a<player/all> " + "&e<time> &e<bonusAmount> ");

                    sender.sendMessage(MythicLib.plugin.parseColors( "&cTime and Boost must be positive!"));
                    return true;
                }
            } else {
                sender.sendMessage(MythicLib.plugin.parseColors("&aOxy SetBonus") +"&a<player/all> " + "&e<time> &e<bonusAmount> ");
            }
        }
        return false;
    }
}

