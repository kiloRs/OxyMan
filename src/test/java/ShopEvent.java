//package com.thebetterchoice.oxyman;//
//
//
//import net.m10653.oxydealer.DrugId;
//import net.m10653.oxydealer.api.DrugSellEvent;
//import net.m10653.oxydealer.core.Main;
//import net.m10653.oxydealer.economy.PersonalBoostHandler;
//import net.m10653.oxydealer.util.ConfigHandler;
//import net.m10653.oxydealer.util.SellList;
//import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
//import org.bukkit.Material;
//import org.bukkit.entity.HumanEntity;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.inventory.InventoryClickEvent;
//import org.bukkit.event.inventory.InventoryCloseEvent;
//import org.bukkit.inventory.Inventory;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.meta.ItemMeta;
//import org.bukkit.plugin.Plugin;
//
//import java.text.DecimalFormat;
//import java.util.*;
//
//public class ShopEvent implements Listener {
//    Map<UUID, Long> clickLimit = new HashMap();
//
//    public ShopEvent() {
//    }
//
//    @EventHandler
//    public void onShopClick(InventoryClickEvent event) {
//        Plugin p = OxyPlugin.getPlugin();
//        Map<Inventory, Boolean> inv = OxyPlugin.getInventories();
//        if (inv.containsKey(event.getInventory())) {
//            Player player = (Player)event.getWhoClicked();
//            ItemStack clicked;
//            String var20;
//            if (inv.get(event.getInventory())) {
//                event.setCancelled(true);
//                clicked = event.getCurrentItem();
//                if (clicked == null) {
//                    return;
//                }
//
//                if (clicked.getType() != Material.PLAYER_HEAD) {
//                    return;
//                }
//
//                Long time = (Long)this.clickLimit.get(player.getUniqueId());
//                if (time != null && System.currentTimeMillis() - time < 3000L) {
//                    var20 = ChatColor.translateAlternateColorCodes('&', ConfigHandler.getConfig().getString("prefix"));
//                    player.sendMessage(var20 + " " + ChatColor.RED + "Please slow down! You don't need to refresh that quickly.");
//                    return;
//                }
//
//                this.clickLimit.put(player.getUniqueId(), System.currentTimeMillis());
//                if (clicked.getItemMeta().getDisplayName().contains("Highest to Lowest")) {
//                    event.getInventory().clear();
//                    SellList.updateInv(player, event.getInventory(), true);
//                } else if (clicked.getItemMeta().getDisplayName().contains("Lowest to Highest")) {
//                    event.getInventory().clear();
//                    SellList.updateInv(player, event.getInventory(), false);
//                } else if (clicked.getItemMeta().getDisplayName().contains("Refresh")) {
//                    event.getInventory().clear();
//                    SellList.updateInv(player, event.getInventory(), true);
//                }
//            }
//
//            clicked = event.getCurrentItem();
//            if (clicked != null) {
//                double multiplier = 1.0D;
//                Iterator var7 = ConfigHandler.getConfig().getConfigurationSection("multipliers").getKeys(false).iterator();
//
//                double tax;
//                while(var7.hasNext()) {
//                    String s = (String)var7.next();
//                    if (player.hasPermission("oxydealer.multiplier." + s)) {
//                        tax = ConfigHandler.getConfig().getDouble("multipliers." + s);
//                        if (tax < 0.0D) {
//                            tax = Math.abs(tax) / 100.0D;
//                        } else {
//                            tax /= 100.0D;
//                        }
//
//                        multiplier += tax;
//                    }
//                }
//
//                multiplier += Main.globalBoost.getCurrentboost();
//                if (Main.playerBoosts.containsKey(player.getUniqueId())) {
//                    multiplier += ((PersonalBoostHandler)Main.playerBoosts.get(player.getUniqueId())).getCurrentboost();
//                }
//
//                if (clicked.hasItemMeta() && clicked.getItemMeta().hasDisplayName()) {
//                    if (clicked.getItemMeta().getDisplayName().contains("> Sell Drugs") && clicked.getType() == Material.LIME_STAINED_GLASS_PANE) {
//                        Bukkit.getLogger().info("sell drugs");
//                        event.setCancelled(true);
//                        double price = this.getPrice(event.getInventory());
//                        if (price < 0.0D) {
//                            var20 = ChatColor.translateAlternateColorCodes('&', ConfigHandler.getConfig().getString("prefix"));
//                            player.sendMessage(var20 + ChatColor.RED + " You had no drugs to sell!");
//                            event.setCancelled(true);
//                            return;
//                        }
//
//                        this.influence(event.getInventory(), player);
//                        HumanEntity var10000 = event.getWhoClicked();
//                        var20 = ChatColor.translateAlternateColorCodes('&', ConfigHandler.getConfig().getString("prefix"));
//                        var10000.sendMessage(var20 + ChatColor.GREEN + " You sold Â£" + (new DecimalFormat("#,###.00")).format(price * multiplier) + " worth of drugs");
//                        if (Main.market.getCurrentEvent() != null) {
//                            boolean sendMessage = Main.market.getCurrentEvent().getAffects().isEmpty();
//
//                            for(int i = 0; i < Main.market.getCurrentEvent().getAffects().size() && !sendMessage; ++i) {
//                                if (event.getInventory().contains(((DrugId)Main.market.getCurrentEvent().getAffects().get(i)).getMat())) {
//                                    sendMessage = true;
//                                }
//                            }
//
//                            if (sendMessage) {
//                                double curE = Main.market.getCurrentEvent().getModifier();
//                                if (curE > 0.0D) {
//                                    var10000 = event.getWhoClicked();
//                                    var20 = ChatColor.translateAlternateColorCodes('&', ConfigHandler.getConfig().getString("prefix"));
//                                    var10000.sendMessage(var20 + ChatColor.GREEN + " You gained " + curE + "%");
//                                } else {
//                                    var10000 = event.getWhoClicked();
//                                    var20 = ChatColor.translateAlternateColorCodes('&', ConfigHandler.getConfig().getString("prefix"));
//                                    var10000.sendMessage(var20 + ChatColor.DARK_RED + " You lost " + curE + "%");
//                                }
//                            }
//                        }
//
//                        Main.economy.depositPlayer(player, price * multiplier);
//
//                        for(int i = 0; i < event.getInventory().getSize(); ++i) {
//                            ItemStack curItem = event.getInventory().getItem(i);
//                            if (curItem != null && Main.market.isDrug(new DrugId(curItem.getType(), curItem.getDurability()))) {
//                                event.getInventory().setItem(i, (ItemStack)null);
//                            }
//                        }
//                    } else if (clicked.getItemMeta().getDisplayName().contains("> Click to cancel") && clicked.getType() == Material.RED_STAINED_GLASS_PANE) {
//                        event.setCancelled(true);
//                        Bukkit.getLogger().info("cancel");
//                        ((HumanEntity)event.getViewers().get(0)).closeInventory();
//                    } else if (clicked.getType() == Material.PLAYER_HEAD) {
//                        event.setCancelled(true);
//                        Bukkit.getLogger().info("calculate");
//                        if (clicked.getItemMeta().getDisplayName().contains("> Calculate Price")) {
//                            ItemStack priceButton = event.getInventory().getItem(45);
//                            ItemMeta meta = priceButton.getItemMeta();
//                            tax = this.getPrice(event.getInventory());
//                            if (tax < 0.0D) {
//                                tax = 0.0D;
//                            }
//
//                            ChatColor var10001 = ChatColor.DARK_GRAY;
//                            meta.setLore(List.of(var10001 + "Sale Price: " + ChatColor.YELLOW + "Â£" + (new DecimalFormat("#,###.00")).format(tax * multiplier)));
//                            priceButton.setItemMeta(meta);
//                            event.getInventory().setItem(45, priceButton);
//                        }
//                    } else if (clicked.getType() == Material.PLAYER_HEAD) {
//                        event.setCancelled(true);
//                    }
//                }
//            }
//        }
//
//    }
//
//    @EventHandler
//    public void onShopClose(InventoryCloseEvent event) {
//        if (((Main)Main.getPlugin(Main.class)).getInventories().containsKey(event.getInventory()) && !(Boolean)((Main)Main.getPlugin(Main.class)).getInventories().get(event.getInventory())) {
//            boolean drop = false;
//
//            int i;
//            for(i = 0; i < 45; ++i) {
//                if (event.getInventory().getItem(i) != null) {
//                    drop = true;
//                }
//            }
//
//            if (drop) {
//                HumanEntity var10000 = event.getPlayer();
//                String var10001 = ChatColor.translateAlternateColorCodes('&', ConfigHandler.getConfig().getString("prefix"));
//                var10000.sendMessage(var10001 + ChatColor.RED + "You cancelled the transaction; your items have been dropped on the ground!");
//
//                for(i = 0; i < 45; ++i) {
//                    if (event.getInventory().getItem(i) != null) {
//                        event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), event.getInventory().getItem(i));
//                    }
//                }
//            }
//        }
//
//    }
//
//    private double getPrice(Inventory inv) {
//        Map<DrugId, Integer> drugs = this.getDrugs(inv);
//        if (drugs.size() <= 0) {
//            return -1.0D;
//        } else {
//            double price = 0.0D;
//
//            DrugId drugId;
//            for(Iterator var6 = drugs.keySet().iterator(); var6.hasNext(); price += Main.market.sell(drugId, (Integer)drugs.get(drugId))) {
//                drugId = (DrugId)var6.next();
//            }
//
//            return price;
//        }
//    }
//
//    private void influence(Inventory inv, Player player) {
//        Map<DrugId, Integer> drugs = this.getDrugs(inv);
//
//        DrugId drugId;
//        int amountOfItems;
//        for(Iterator var5 = drugs.keySet().iterator(); var5.hasNext(); Bukkit.getServer().getPluginManager().callEvent(new DrugSellEvent(drugId, amountOfItems, player))) {
//            drugId = (DrugId)var5.next();
//            amountOfItems = (Integer)drugs.get(drugId);
//            if (!player.hasPermission("oxydealer.ignore")) {
//                Main.market.influence(drugId, (Integer)drugs.get(drugId));
//            }
//        }
//
//    }
//
//    private Map<DrugId, Integer> getDrugs(Inventory inv) {
//        Map<DrugId, Integer> drugs = new HashMap();
//        Arrays.stream(inv.getContents()).filter(Objects::nonNull).forEach((item) -> {
//            DrugId drugId = new DrugId(item.getType(), item.getDurability());
//            if (Main.market.isDrug(drugId)) {
//                if (drugs.containsKey(drugId)) {
//                    drugs.replace(drugId, (Integer)drugs.get(drugId) + item.getAmountOfItems());
//                } else {
//                    drugs.put(drugId, item.getAmountOfItems());
//                }
//            }
//
//        });
//        return drugs;
//    }
//}
