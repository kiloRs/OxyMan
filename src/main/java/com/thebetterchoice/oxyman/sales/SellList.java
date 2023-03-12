package com.thebetterchoice.oxyman.sales;

import com.thebetterchoice.oxyman.OxyPlugin;
import com.thebetterchoice.oxyman.PermissionInstance;
import com.thebetterchoice.oxyman.PermissionLoader;
import com.thebetterchoice.oxyman.Stats;
import com.thebetterchoice.oxyman.drugs.RegisteredDrug;
import com.thebetterchoice.oxyman.specialevents.SpecialEvent;
import com.thebetterchoice.oxyman.utils.MessageConstants;
import com.thebetterchoice.oxyman.utils.NavigationReference;
import io.lumine.mythic.lib.MythicLib;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.Indyuce.mmoitems.api.item.template.MMOItemTemplate;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SellList {
    private static ItemStack filler = null;
    private static ItemStack upSort = null;
    private static ItemStack downSort = null;
    private static ItemStack refresh = null;
    public static final List<RegisteredDrug> drugs = new ArrayList<>();

    public SellList() {
        loadItems();
    }

    private void loadItems() {
        Type n = Stats.NAVIGATION;
        String path = "FILLER";
        loadTemplate(n, path, "Loaded FILLER for Navigation System!");
        OxyPlugin.log("Found FILLER Item");
        filler = MMOItems.plugin.getItem(n, path);
        path = "LOW";
        loadTemplate(n, path, "Loaded " + path + " for Navigation System!");
        OxyPlugin.log("Found " + path + " Item");
        downSort = MMOItems.plugin.getItem(n, path);
        path = "HIGH";
        loadTemplate(n, path, "Loaded " + path + " for Navigation System!");
        OxyPlugin.log("Found " + path + " Item");
        upSort = MMOItems.plugin.getItem(n, path);
        path = "REFRESH";
        loadTemplate(n, path, "Loaded " + path + " for Navigation System!");
        OxyPlugin.log("Found " + path + " Item");
        refresh = MMOItems.plugin.getItem(n, path);
    }

    private void loadTemplate(Type n, String path, String log) {
        if (!MMOItems.plugin.getTemplates().hasTemplate(n, path)) {
            MMOItemTemplate temp = new MMOItemTemplate(n, path);
            MMOItems.plugin.getTemplates().registerTemplate(temp);
            OxyPlugin.log(log);
        }
    }
    public static void updateInv(Player player, Inventory inv, boolean assending) {
        if (filler == null || upSort == null || downSort == null || refresh == null) {
            filler = NavigationReference.getFiller();
            upSort = NavigationReference.getHighToLow();
            downSort = NavigationReference.getLowToHigh();
            refresh = NavigationReference.getRefresh();
        }

        inv.clear();
        int multiplier = 0;


        for (PermissionInstance instance : PermissionLoader.getPermissionsUsed()) {
            if (!OxyPlugin.getPermissionLoader().doesPlayerHave(instance,player)) {
                continue;
            }
            multiplier += OxyPlugin.getPermissionLoader().getValueOfPermission(instance);

        }

        multiplier += (int)(OxyPlugin.getGlobalBonus().getCurrentBonus() * 100.0D);
        if (OxyPlugin.playerBoosts.containsKey(player.getUniqueId())) {
            multiplier += (int)((OxyPlugin.playerBoosts.get(player.getUniqueId())).getCurrentBonus()    * 100.0D);
        }

        ItemMeta fillMeta = filler.getItemMeta();
        if (fillMeta == null){
            throw new RuntimeException("Invalid Filler Refresh Meta");
        }
        if (OxyPlugin.getMarket().getCurrentEvent() != null && OxyPlugin.getMarket().getEventReSetter() != null) {
            String num = OxyPlugin.getMarket().getEventReSetter().getTimeLeft();

          //  fillMeta.setLore(Arrays.asList(ChatColor.GRAY + "An event is currently active;", ChatColor.GRAY + "It will end in " + time, ChatColor.GRAY + "Check " + ChatColor.RED + "/sell"));
            fillMeta.setLore(List.of(MythicLib.inst().parseColors(MessageConstants.get().getMENU_EVENT_ACTIVE().replace("%formatted_time%",num))));
        } else {
          //  fillMeta.setLore(Arrays.asList(ChatColor.GRAY + "Hover over the buttons", ChatColor.GRAY + "to see extra actions!"));
            fillMeta.setLore(List.of(MythicLib.inst().parseColors(MessageConstants.get().getLORE_FILLER())));
        }

        filler.setItemMeta(fillMeta);
        sortDrugs(drugs, assending);

        for (RegisteredDrug drug : drugs) {
            MMOItem b = drug.getItemTemplate().newBuilder().build();
            b.newBuilder().getLore().registerPlaceholder("sell",multiplier>0?"Sell Price:" + MessageConstants.get().getCURRENCY()  + getDecimalFormat().format(OxyPlugin.getMarket().sell(drug, 1.0)) + MessageConstants.get().getCURRENCY() + getDecimalFormat().format(OxyPlugin.getMarket().sell(drug, 1.0) * ((double) multiplier / 100.0D + 1.0D)):"Sell Price:" +  " $" + getDecimalFormat().format(OxyPlugin.getMarket().sell(drug, 1.0)));
            b.newBuilder().getLore().registerPlaceholder("sellAll",multiplier>0?"Sell Price Per Stack: " + "$" + getDecimalFormat().format(OxyPlugin.getMarket().sell(drug, 64.0)) +  " $" + getDecimalFormat().format(OxyPlugin.getMarket().sell(drug, 64.0) * ((double) multiplier / 100.0D + 1.0D)):"Sell Price Per Stack: " +  "$" + getDecimalFormat().format(OxyPlugin.getMarket().sell(drug, 64.0)));
            String path = b.newBuilder().getLore().applySpecialPlaceholders("{sell}");
            String pathAll = b.newBuilder().getLore().applySpecialPlaceholders("{sellAll}");

            b.newBuilder().getLore().getLore().set(b.newBuilder().getLore().getLore().size() - 2,path );
            b.newBuilder().getLore().getLore().set(b.newBuilder().getLore().getLore().size() - 1, pathAll );
//            b.newBuilder().getLore().end("Items Sold: " + MythicLib.inst().getMMOConfig().newDecimalFormat("#,###.00").format((double) drug.getDrug().getDemand()!=drug.getStartingDemand()?drug.getDrug().getDemand():drug.getStartingDemand() - ConfigReference.serverDemandBase));
            List<String> lore = b.newBuilder().getLore().build();

            ItemStack item = b.newBuilder().buildSilently();
            ItemMeta meta = item.getItemMeta();
            if (meta ==null){
                OxyPlugin.debug("No Meta for " + drug.getPath());
                continue;
            }
            meta.setLore(lore);

//            ItemMeta meta = item.getItemMeta();
//            ArrayList<String> lore = new ArrayList<>();
//            if (multiplier > 0) {
//                lore.add(MythicLib.inst().parseColors("Sell Price:" + MessageConstants.get().getCURRENCY()  + getDecimalFormat().format(OxyPlugin.getMarket().sell(drug, 1.0)) + MessageConstants.get().getCURRENCY() + getDecimalFormat().format(OxyPlugin.getMarket().sell(drug, 1.0) * ((double) multiplier / 100.0D + 1.0D))));
//            } else {
//                lore.add( MythicLib.inst().parseColors("Sell Price:" +  " $" + getDecimalFormat().format(OxyPlugin.getMarket().sell(drug, 1.0))));
//            }
//
//            if (multiplier > 0) {
//                lore.add( MythicLib.inst().parseColors("Sell Price Per Stack: " + "$" + getDecimalFormat().format(OxyPlugin.getMarket().sell(drug, 64.0)) +  " $" + getDecimalFormat().format(OxyPlugin.getMarket().sell(drug, 64.0) * ((double) multiplier / 100.0D + 1.0D))));
//            } else {
//                lore.add(MythicLib.inst().parseColors( "Sell Price Per Stack: " +  "$" + getDecimalFormat().format(OxyPlugin.getMarket().sell(drug, 64.0))));
//            }

//            lore.add(MythicLib.inst().parseColors( "Items Sold: " + MythicLib.inst().getMMOConfig().newDecimalFormat("#,###.00").format((double) drug.getDrug().getDemand()!=drug.getStartingDemand()?drug.getDrug().getDemand():drug.getStartingDemand() - ConfigReference.serverDemandBase)));
            SpecialEvent event = OxyPlugin.getMarket().getCurrentEvent();
            if (event != null && (event.getAffects().isEmpty() || event.getAffects().contains(drug))) {
                String display = MessageConstants.get().getDISPLAY_PRICE_TOP();
                if (event.getModifier() >= 0.0D) {
                    display = display + " " + ChatColor.GREEN + MessageConstants.get().getDISPLAY_PRICE_TOP_PERCENTAGE();
                } else {
                    display = display + " " + ChatColor.RED + MessageConstants.get().getDISPLAY_PRICE_TOP_PERCENTAGE().replace("%value%",event.getModifier() + "");
                }

                lore.add(display);
            }

//            meta.setLore(lore);
//            item.setItemMeta(meta);
            inv.addItem(item);
            int start = inv.getSize() - 9;

            for (int i = start; i < inv.getSize(); ++i) {
                if (i % 9 == 3) {
                    inv.setItem(i, upSort);
                } else if (i % 9 == 4) {
                    inv.setItem(i, refresh);
                } else if (i % 9 == 5) {
                    inv.setItem(i, downSort);
                } else {
                    inv.setItem(i, filler);
                }
            }
        }

    }

    private static DecimalFormat getDecimalFormat() {
        return MythicLib.inst().getMMOConfig().newDecimalFormat(MessageConstants.get().getDECIMAL());
    }

    public static void sortDrugs(List<RegisteredDrug> drugs, boolean ascending) {
        Comparator<RegisteredDrug> comparator = (c1, c2) -> ascending ? (int)(OxyPlugin.getMarket().sell(c2, 1.0) - OxyPlugin.getMarket().sell(c1, 1.0)) : (int)(OxyPlugin.getMarket().sell(c1, 1.0) - OxyPlugin.getMarket().sell(c2, 1.0));
        drugs.sort(comparator);
    }
}
