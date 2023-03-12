package com.thebetterchoice.oxyman.sales;

import com.jeff_media.jefflib.ArrayUtils;
import com.thebetterchoice.oxyman.OxyPlugin;
import com.thebetterchoice.oxyman.drugs.DrugId;
import com.thebetterchoice.oxyman.drugs.DrugRegistry;
import com.thebetterchoice.oxyman.drugs.RegisteredDrug;
import com.thebetterchoice.oxyman.specialevents.SpecialEvent;
import com.thebetterchoice.oxyman.specialevents.SpecialEventHandler;
import com.thebetterchoice.oxyman.specialevents.SpecialEventListener;
import com.thebetterchoice.oxyman.utils.ConfigReference;
import com.thebetterchoice.oxyman.utils.MessageConstants;
import com.thebetterchoice.oxyman.utils.OxyLogger;
import io.lumine.mythic.lib.MythicLib;
import lombok.Getter;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

public class Market implements SpecialEventListener, InventoryHolder {
    private final SpecialEventHandler eHandler;
    private final int size;
    private SpecialEvent currentEvent;
    private EventReset eventReSetter;
    @Getter
    private final Plugin plugin;
//    private final Map<RegisteredDrug, Double> drugDemand = DrugRegistry.getDemand();


    public Market(Plugin plugin) {
        this.plugin = plugin;

        FileConfiguration config = OxyPlugin.getPlugin().getConfig();
        String id = "Market";

        String path = "Launcher.";
        long repeat = config.getLong(path + id +".Repeat",10);
        int max = config.getInt(path + id +".Max",600);
        int min = config.getInt(path + id +".Min",1);
        double chance = config.getDouble(path +id +".Chance",0.35);
        this.eHandler = new SpecialEventHandler(min, max, Math.toIntExact(repeat), chance);
        OxyPlugin.debug("Loading Main File in Market");

        String name = config.getString("Shop.Market.Name", "&aMarket");
        this.size = config.getInt("Shop.Market.Size", InventoryType.CHEST.getDefaultSize());


        beginMarketSystem();

        //This
        //todo check math

        BukkitRunnable runnable = new BukkitRunnable() {
            public void run() {
                for (RegisteredDrug registeredDrug : DrugRegistry.getRegisteredDrugs().values()) {
                    int updated = 0;
//                    if (drugDemand.containsKey(registeredDrug) && drugDemand.get(registeredDrug)==registeredDrug.getDrug().getDemand()){
//                        updated = updated + 1;
//                        continue;
//                    }
//                    double start = loadingDrug.getDemand();
                    double max = ConfigReference.serverDemandMax;
                    double min = ConfigReference.serverDemandMin;



                    //This
//                    double demandAmount = loadingDrug.getDemand();
//                    long temp = (long) ((demandAmount - start) * amount);

//                    if (temp + ((long) start) > start) {
//                        loadingDrug.setDemand((long) (temp + start));
                    }
//                    if (drugDemand.containsKey(registeredDrug) && drugDemand.get(registeredDrug)==registeredDrug.getDrug().getDemand()){
//                        updated = updated + 1;
//                        continue;
//                    }
//                    DemandSetEvent event = new DemandSetEvent(registeredDrug, temp + start > start ? (long) (temp + start) : demandAmount);
//                    Bukkit.getPluginManager().callEvent(event);
//                    double increase = (temp + start);
//                    double baseValue = loadingDrug.getDemand() - increase;

//                    if (event.isCancelled() || event.getDrugUsed() == null || !DrugRegistry.hasDrug(registeredDrug.getItem())) {
//                        if (temp + ((long) start) > start){
//                            loadingDrug.setDemand((long) baseValue);
//                        }
//                        OxyPlugin.debug("Cancelled Event: DemandSetEvent for " + registeredDrug.getPath() + " removing " + increase + " || "  + baseValue);
//                        continue;
//                    }
//
//                    OxyPlugin.debug("Event Sent: " + registeredDrug.getPath() + " amountOfItems value: " + loadingDrug.getDemand());
//
//                    drugDemand.put(registeredDrug, registeredDrug.getDrug().getDemand());
                    //todo check math
                }
        };
        runnable.runTaskTimer(OxyPlugin.getPlugin(), (ConfigReference.reductionLength + 1) * 1200L, 1200L * ConfigReference.reductionRepeat);

        int taskId = runnable.getTaskId();

        if (Bukkit.getScheduler().isCurrentlyRunning(taskId)){
            OxyLogger.debug("Running Scheduled: " + taskId);
        }
        else if (Bukkit.getScheduler().isQueued(taskId)){
            OxyLogger.debug("Queued Task: " + taskId);
        }

    }

    private void beginMarketSystem() {
        List<SpecialEvent> events = SpecialEvent.parseEvents(OxyPlugin.getEventsConfig().getConfig());
        this.eHandler.addEvents(events);
        this.eHandler.addListener(this);
        this.eHandler.start();

    }


    public void addDrug(RegisteredDrug loadingDrug) {
        DrugRegistry.register(loadingDrug.getItem().getType().getId(), loadingDrug.getItem().getId());
        OxyPlugin.debug("Adding LoadingDrug to Register: " + loadingDrug.getItem().getId());
    }

//    public void addDrugs(List<LoadingDrug> drugs) {
//        if (drugs != null) {
//
//            for (LoadingDrug d : drugs) {
//                this.addDrug(d);
//            }
//        }
//
//    }
//
//    public List<RegisteredDrug> getDrugs() {
//        return this.drugDemand.keySet().stream().toList();
//    }
//
//    public void influence(RegisteredDrug drug, int amount) {
//            OxyPlugin.log("Influenced " + drug.getPath() + " to amountOfItems of " + drug.getDrug().getDemand() + amount);
//
//            drug.getDrug().addDemand((double) amount);
//            drugDemand.put(drug, drug.getDrug().getDemand());
//
//    }

    public double sell(DrugId drugId, Double amount) {
        double price = 0.0D;

        for(int i = 0; i < amount; ++i) {
//            price += this.calculate(drugId, i, true);
        }


        return price;
    }

    public double sellIgnoreEvent(DrugId drugId, int amount) {
        double price = 0.0D;

        for(int i = 0; i < amount; ++i) {
//            price += this.calculate(drugId, i, false);
        }

        return price;
    }


//        if (DrugRegistry.getRegisteredDrugs().size() > 0 && temp != null) {
//            OxyPlugin.debug("Offset: " + offset);
//            RegisteredDrug d = null;
//            for(var var8 = drugDemand.keySet().iterator(); var8.hasNext();) {
//                d = var8.next();
//                total = offset + drugDemand.get(var8.next());
//            }
//
//            double avg = (total - (temp.getDemand() + (long) offset)) / (double)(this.drugDemand.size() - 1);
//            double price = temp.getPrice() * (avg / (temp.getDemand() + (double) offset));
//
//            if (d == null){
//                throw new RuntimeException("Invalid LoadingDrug in Market");
//            }
//            double max = d.getMaxValue()>0?d.getMaxValue():ConfigReference.serverDemandMax;
//            double min = d.getMinValue()==0?ConfigReference.serverDemandMin:d.getMinValue();
////            double max = temp.getDrugAsMMOItem().hasData(Stats.getMaxCost())?((DoubleData) temp.getDrugAsMMOItem().getData(Stats.getMaxCost())).getDefaultValue():config.getInt("price.max",-1);
////            double min = temp.getDrugAsMMOItem().hasData(Stats.getMinCost())?((DoubleData) temp.getDrugAsMMOItem().getData(Stats.getMinCost())).getDefaultValue():config.getInt("price.min",0);
//
//            if (max==-1){
//                if (price>min){
//                    if (this.currentEvent != null && useEvent && (this.currentEvent.getAffects().isEmpty() || this.currentEvent.getAffects().contains(d))) {
//                        double eventMod = this.currentEvent.getModifier();
//                        eventMod /= 100.0D;
//                        price += (price * eventMod);
//                        OxyPlugin.debug("Item: " + temp.getDisplayName() + " " + "Price: " + price + " Event Modifier: " + eventMod + " Current Event: " + this.currentEvent.getPath());
//                    }
//                }
//            }
//            else {
//                if (price>max){
//                    price = max;
//                }
//                if (price<min){
//                    price = min;
//                }
//            }
//
//            double serverMaxPrice = ConfigReference.serverMaxCost;
//            double serverMinPrice = ConfigReference.serverMinCost;
//
//            if (serverMaxPrice != -1 && price > serverMaxPrice){
//                price = serverMaxPrice;
//            }
//            if (serverMinPrice != -1 && price < serverMinPrice){
//                price = serverMinPrice;
//            }
//
//
//            OxyPlugin.debug("Final Item: " + temp.getDisplayName() + " " + "Final Price: " + price +   " Current Event: " + this.currentEvent.getPath());
//
//            return price;
//        } else {
//            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Bad Event Claim: Error 54");
//            return 0.0D;
//        }


    public SpecialEvent getCurrentEvent() {
        return this.currentEvent;
    }

    public SpecialEventHandler getEventHandler() {
        return this.eHandler;
    }

    public void setCurrentEvent(SpecialEvent currentEvent, int duration, boolean override) {
        if (this.currentEvent == null || override) {
            this.currentEvent = currentEvent;
            if (this.eventReSetter != null) {
                Bukkit.getScheduler().cancelTask(this.eventReSetter.getTaskId());
            }

            this.eventReSetter = onEnd(currentEvent,duration + 1,1.25);
            String mod = "";
            if (currentEvent.getModifier() >= 0.0D) {
                mod = "+" + currentEvent.getModifier();
            } else {
                mod = String.valueOf(currentEvent.getModifier());
            }

            String eventdisplay = MessageConstants.get().getEVENT_DISPLAY();
            String prefix = OxyPlugin.getPrefix();
            List<RegisteredDrug> affects = currentEvent.getAffects();
            String[] strings = ArrayUtils.createArray(String.class,affects.size());
            if (!affects.isEmpty()){
                for (RegisteredDrug eachRegisteredDrug : affects) {
                    ArrayUtils.addAfter(strings,eachRegisteredDrug.getPath() + " ");
                }
            }
            String wording = eventdisplay.replaceAll("%prefix%", prefix).replaceAll("%display%", currentEvent.getPath().toUpperCase(Locale.ROOT) + "EVENT").replaceAll("\\|", " ").replace("%affect%", org.apache.commons.lang.ArrayUtils.toString(strings,"All"));
            Bukkit.broadcastMessage(MythicLib.plugin.parseColors(wording));
        }
        else {
            OxyPlugin.log("Cannot Override Current Event!");
        }
    }
    public EventReset getEventReSetter() {
        return this.eventReSetter;
    }


    @NotNull
    public RegisteredDrug getDrug(MMOItem mmoItem){
        if (mmoItem == null){
            throw new RuntimeException("Invalid LoadingDrug Item");
        }
        if (!DrugRegistry.hasDrug(mmoItem)){
            OxyPlugin.debug("Registered LoadingDrug not Found!");
            DrugRegistry.register(mmoItem.getType().getId(),mmoItem.getId());
            OxyPlugin.log("Registering LoadingDrug - " + mmoItem.getId());
            return DrugRegistry.getRegisteredDrugs().get(mmoItem.getId());
        }
        return DrugRegistry.getRegisteredDrugs().get(mmoItem.getId());
    }

    public void drugReset() {
        double defaultD = ConfigReference.serverDemandBase;

//        for (RegisteredDrug drug : this.drugDemand.keySet()) {
//            drug.getDrug().setDemand(((long) defaultD));
//        }

    }

    public void onEvent(SpecialEvent event, int time) {
        this.setCurrentEvent(event, time, false);
        OxyPlugin.log("Assigned Special Event");
    }

    @Override
    public EventReset onEnd(SpecialEvent event, long value, double multiplier) {
        EventReset eventReset = new EventReset(((int) value));

        String message = MythicLib.plugin.parseColors(MessageConstants.get().getPREFIX() + MessageConstants.get().getEVENT_END().replace("%event%", event.getPath()).replace("%formatted_time%", "" + eventReset.getTimeLeft()));
        Bukkit.getServer().broadcast(message,"oxy.event.notify");
        return eventReset;
    }


    @Override
    public void announce(String announceSomething) {
        Logger.getLogger("Minecraft").severe("[Announce] : ; : " + announceSomething);
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return Bukkit.createInventory(this, size, "Market");
    }


    public void addDrugs(List<RegisteredDrug> loadingDrugs) {
        for (RegisteredDrug loadingDrug : loadingDrugs) {
            addDrug(loadingDrug);
        }
    }

    public String getName() {
        return OxyPlugin.getFileConfiguration().contains("Inventory.Name")?OxyPlugin.getFileConfiguration().getString("Inventory.Name"):"&aMarket";
    }


    public class EventReset extends BukkitRunnable {
        private final int time;
        private final long startTime;

        public EventReset(int time) {
            this.time = time;
            this.startTime = System.currentTimeMillis();
            this.runTaskLater(OxyPlugin.getPlugin(), 1200L * (long)time);
        }

        public void run() {
            Market.this.currentEvent = null;
        }

        public int getTime() {
            return this.time;
        }

        public String getTimeLeft() {
            long milliseconds = System.currentTimeMillis() - startTime;
            int seconds = (int) (milliseconds / 1000) % 60 ;
            int minutes = (int) ((milliseconds / (1000*60)) % 60);
            int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
            return hours + ":" + minutes + ":" + seconds;
         }

}
}
