package com.thebetterchoice.oxyman.utils;

import com.thebetterchoice.oxyman.OxyPlugin;
import com.thebetterchoice.oxyman.Stats;
import io.lumine.mythic.lib.api.item.NBTItem;
import lombok.Getter;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.item.build.MMOItemBuilder;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.Indyuce.mmoitems.api.item.template.MMOItemTemplate;
import net.Indyuce.mmoitems.stat.data.StringData;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class NavigationReference {
    @Getter
    public static ItemStack filler;
    @Getter
    public static ItemStack refresh;
    @Getter
    public static ItemStack confirm;
    @Getter
    public static ItemStack cancel;
    @Getter
    public static ItemStack price;
    @Getter
    public static ItemStack sell;
    @Getter
    public static ItemStack skull;
    @Getter
    public static ItemStack highToLow;
    @Getter
    public static ItemStack lowToHigh;
    @Getter
    private static boolean loadedItems = false;
    @Getter
    private Map<String,String> itemsLoaded = new HashMap<>();

    public NavigationReference(){


        ConfigFile navigation = OxyPlugin.getNavigationConfig();
//        if (generate) {
//            c.set("filler.id", "FILLER");
//            c.set("refresh.id", "REFRESH");
//            c.set("confirm.id", "CONFIRM");
//            c.set("cancel.id", "CANCEL");
//            c.set("price.id", "PRICE");
//            c.set("sell.id", "SELL");
//            c.set("low.id", "LOW");
//            c.set("high.id", "HIGH");
//            navigation.save();
//        }

        if (navigation.getConfig().getKeys(false).isEmpty()){
            throw new RuntimeException("Cannot Load Navigation: No Keys Entered");
        }
        loadItems(navigation);


        if (!navigation.exists()){
            navigation.setup();
        }
        Field[] var1 = NavigationReference.class.getFields();
        for (Field field : var1) {
            try {
                if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()) && field.get(null) instanceof ItemStack itemStack) {
                    String itemId = NBTItem.get(itemStack).getString("MMOITEMS_ITEM_ID");


                    includeItemLoader(field.getName(), itemId);
                    navigation.getConfig().set(field.getName() + ".id",itemId);
                    OxyPlugin.log("Loading Navigation: " + field.getName());
                }
            } catch (IllegalAccessException | IllegalArgumentException var6) {
                OxyPlugin.throwError("Bad Fields in Navigation");
            }
            navigation.save();
        }
    }

    public static void loadItems(ConfigFile navigation) {
        Type nav = Stats.NAVIGATION;

        int tracking = 0;
        String id = "";
        MMOItemTemplate sell = new MMOItemTemplate(nav,id);
        MMOItem refresh = loadExtracted(extracted(id, navigation, "refresh.id", "REFRESH", sell, nav, "No Refresh Template Found", NavigationReference.refresh));
        if (refresh != null){
            NavigationReference.refresh = refresh.newBuilder().buildSilently();
            OxyPlugin.explain("Loaded Refresh Navigation Object",false);
            ++tracking;
        };

        MMOItem priceMMOItem = loadExtracted(extracted(id, navigation, "price.id", "PRICE", sell, nav, "No Price Template Found", NavigationReference.price));
        if (priceMMOItem != null){
            NavigationReference.price = priceMMOItem.newBuilder().buildSilently();
            OxyPlugin.explain("Loaded Price Navigation Object",false);
            ++tracking;

        }
        MMOItem confirmMMOitem = loadExtracted(extracted(id, navigation, "confirm.id", "CONFIRM", sell, nav, "No Confirm Template Found", NavigationReference.confirm));
        if (confirmMMOitem != null){
            NavigationReference.confirm = confirmMMOitem.newBuilder().buildSilently();
            OxyPlugin.explain("Loaded Confirm Navigation Object",false);

            ++tracking;
        }
        MMOItem cancelMMO = loadExtracted(extracted(id, navigation, "cancel.id", "CANCEL", sell, nav, "No Cancel Template Found", NavigationReference.cancel));
        if (cancelMMO != null){
            cancel = cancelMMO.newBuilder().buildSilently();
            OxyPlugin.explain("Loaded Cancel Navigation Object",false);

            ++tracking;
        }
        MMOItem highMMO = loadExtracted(extracted(id, navigation, "high.id", "HIGH", sell, nav, "No High Template Found", NavigationReference.highToLow));
        if (highMMO != null){
            highToLow = highMMO.newBuilder().buildSilently();
            OxyPlugin.explain("Loaded High to Low Navigation Object",false);

            ++tracking;
        }
        MMOItem lowMMO = loadExtracted(extracted(id, navigation, "low.id", "LOW", sell, nav, "No LOW Template Found", NavigationReference.lowToHigh));
        if (lowMMO != null){
            lowToHigh = lowMMO.newBuilder().buildSilently();
            OxyPlugin.explain("Loaded Low to High Navigation Object",false);

            ++tracking;
        }

        MMOItem skullMMO = loadExtracted(extracted(id, navigation, "skull.id", "SKULL", sell, nav, "No Skull Template Found", NavigationReference.skull));
        if (skullMMO != null){
            skull = skullMMO.newBuilder().buildSilently();
            OxyPlugin.explain("Loaded Low to High Navigation Object",false);

            ++tracking;
        }
        MMOItem sellMMO = loadExtracted(extracted(id, navigation, "sell.id", "SELL", sell, nav, "Template Not Found Sell", NavigationReference.sell));
        if (sellMMO != null){
            NavigationReference.sell = sellMMO.newBuilder().buildSilently();
            OxyPlugin.explain("Loaded Sell Navigation Object",false);

            ++tracking;
        }

        OxyPlugin.debug("Loaded " + tracking + " items for navigation.");

        if (tracking>=7){
            loadedItems = true;
        }
    }

    public static MMOItem loadExtracted(ItemStack itemStack){
        if (itemStack == null || !NBTItem.get(itemStack).hasType() || itemStack.getType()==Material.AIR) {
            return null;
        }
        String i = MMOItems.getID(itemStack);
        OxyPlugin.debug("Loading Item: " + i);
        Type t = MMOItems.getType(itemStack);
        StringData statData = new StringData("ExtractedTestItem");
        if (!MMOItems.plugin.getTemplates().hasTemplate(t, i)){
            MMOItems.plugin.getTemplates().registerTemplate(new MMOItemTemplate(t,i));
            OxyPlugin.debug("Registering Template for " + i + " of " + t);
        }
        MMOItemTemplate temp = MMOItems.plugin.getTemplates().getTemplate(t, i);
        if (temp == null){
            throw new RuntimeException("Bad MMOItem Issue");
        }
        MMOItemBuilder builder = temp.newBuilder();
        builder.applyData(Stats.view, statData);
        return MMOItems.plugin.getTemplates().hasTemplate(t, i)? builder.build():null;
    }
    private static ItemStack extracted(String id, ConfigFile navigation, String path, String REFRESH, MMOItemTemplate sell, Type nav, String No_Refresh_Template_Found, ItemStack refresh) {
        id = navigation.getConfig().getString(path, REFRESH);
        sell = new MMOItemTemplate(nav, id) ;
        if (!MMOItems.plugin.getTemplates().hasTemplate(nav, id)){
            MMOItems.plugin.getTemplates().registerTemplate(sell);
            sell = MMOItems.plugin.getTemplates().getTemplate(nav, id);
        }
        else {
            sell = MMOItems.plugin.getTemplates().getTemplate(nav, id);
        }
        if (sell == null){
            throw new RuntimeException(No_Refresh_Template_Found);
        }
        refresh = sell.newBuilder().build().newBuilder().buildSilently();
        if (sell == null || !MMOItems.plugin.getTemplates().hasTemplate(nav,id)){
            return null;
        }

        return refresh;
    }


    public void includeItemLoader(String name, String id){
        itemsLoaded.put(name,id);
    }
    public void debug(){
        OxyPlugin.debug("Navigation Reference Load!");
    }

//    public void createItem(String id) {
//        createItem(Stats.navigation,id);
//    }
}
