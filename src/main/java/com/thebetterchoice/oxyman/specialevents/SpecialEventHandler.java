package com.thebetterchoice.oxyman.specialevents;

import com.thebetterchoice.oxyman.OxyPlugin;
import com.thebetterchoice.oxyman.RandomizedGroup;
import com.thebetterchoice.oxyman.drugs.RegisteredDrug;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class SpecialEventHandler {
    public static String BASE = "Market";
    private static String path;
    private final List<SpecialEventListener> listeners = new ArrayList<>();
    private final RandomizedGroup<SpecialEvent> events = new RandomizedGroup<>();
    private final List<SpecialEvent> eventList = new ArrayList<>();
    private final Random rand = new Random(13151);
    private final String name;
    private final int minTime;
    private final int maxTime;
    private final int repeatTime;
    private final double chance;


        public static SpecialEventHandler get(){


            SpecialEventHandler.path = "Special.Events.Handler." + BASE;
            ConfigurationSection c = OxyPlugin.getFileConfiguration();


//            if (!c.isConfigurationSection(path)){
//                HashMap<String, Object> map = new HashMap<>();
//                map.put("Min",1);
//                map.put("Max",300);
//                map.put("Delay",60);
//                map.put("Chance",0.3);
//               c = c.createSection(path, map);
//                OxyPlugin.getPlugin().saveConfig();
//            }
//            else {
//                c = c.getConfigurationSection(path);
//            }
//
//            if (c == null){
//                c = OxyPlugin.getFileConfiguration();
//                HashMap<String, Object> map = new HashMap<>();
//                map.put("Min",-1);
//                map.put("Max",-1);
//                map.put("Delay",1000);
//                map.put("Chance",0.2);
//                c.createSection(path, map);
//                OxyPlugin.getPlugin().saveConfig();
//            }
//
//            if (c == null){
//                throw new RuntimeException("Configuration Section " + path + " not located in SpecialEventHandler Parsing...");
//            }

            int delay = c.getInt(path + "Delay", 60);
            int max = c.getInt(path+"Max", 300);
            int min = c.getInt(path+"Min", 1);
            double chance = c.getDouble(path+"Chance", 0.5);
            return new SpecialEventHandler(min,max,delay,chance);
        }

        public SpecialEventHandler(int minTime, int maxTime, int repeatTime, double chance) {
            this.name = BASE;
            this.minTime = minTime;
            this.maxTime = maxTime;
            this.repeatTime = repeatTime;
            this.chance = chance;
            SpecialEventHandler.path = "Special.Events.Handler." + name;


//
////            if (!knownConfigurationSection.getKeys(false).contains("Special") || !knownConfigurationSection.isConfigurationSection("Special.Events.Handler." + i)){
////                HashMap<String, Object> map = new HashMap<>();
////                map.put("Min",this.minTime);
////                map.put("Max",this.maxTime);
////                map.put("Delay",this.repeatTime);
////                map.put("Chance",this.chance);
////                knownConfigurationSection = OxyPlugin.getFileConfiguration().createSection(path,map);
////                OxyPlugin.getConfigFile().save();
////            }
////            else {
////                knownConfigurationSection = OxyPlugin.getFileConfiguration().getConfigurationSection(path);
////            }
//
//            if (knownConfigurationSection == null){
//                throw new RuntimeException("BAD SPECIAL EVENT HANDLING ON CONFIGURATION SECTION!");
//            }
        }

        public void start() {
            if (this.events.next() == null) {
                Bukkit.getLogger().severe("Unable to Start Events!");
            } else {
                EventTask eventTask = new EventTask(OxyPlugin.isDebug());
                eventTask.runTaskTimer(OxyPlugin.getPlugin(), (long) (1200L * 0.25), 1200L * (long)this.repeatTime);
                OxyPlugin.debug("Starting Event " + eventTask.name + " as task: " + eventTask.getTaskId());
            }

        }

        public boolean isCorrectItem(SpecialEvent e, RegisteredDrug drug){
            return SpecialEvent.items.contains(drug);
        }
        private void update(SpecialEvent event, int time) {

            for (SpecialEventListener l : this.listeners) {
                l.onEvent(event, time);
            }

        }

        public void addEvent(SpecialEvent event) {
            this.events.add(event.getWeight(), event);
            this.eventList.add(event);
        }

        public void addEvents(List<SpecialEvent> events) {
            for (SpecialEvent e : events) {
                this.addEvent(e);
            }

        }

        public List<SpecialEvent> getEvents() {
            return this.eventList;
        }

        public void addListener(SpecialEventListener event) {
            this.listeners.add(event);
        }

        public void removeListener(SpecialEventListener event) {
            this.listeners.remove(event);
        }

        public class EventTask extends BukkitRunnable {

            private final boolean debug;
            private final String name;

            public EventTask(boolean debug) {
                this.debug = debug;
                this.name = SpecialEventHandler.this.name.toUpperCase(Locale.ROOT);

            }

            public void run() {
                double r = Math.random();
                if (r <= SpecialEventHandler.this.chance) {
                    int value = SpecialEventHandler.this.maxTime - SpecialEventHandler.this.minTime + 1;
                    int time = SpecialEventHandler.this.minTime + SpecialEventHandler.this.rand.nextInt(value<=0? minTime + 1 :value);
                    SpecialEvent nextEvent = SpecialEventHandler.this.events.next();
                    SpecialEventHandler.this.update(nextEvent, time);
                   if (debug){
                       OxyPlugin.debug("Special Event Handler: " + this.name + " has attempted to launch an event ith the chance of " + SpecialEventHandler.this.chance);
                       OxyPlugin.debug("Random == " + r);
                       OxyPlugin.debug("Value: " + value + " Time: " + time);
                       OxyPlugin.debug("Event: " + nextEvent.getPath() + " Multiplier: " + nextEvent.getModifier() + " All?: " + nextEvent.isAnyItem() + " Weight: " + nextEvent.getWeight() + " ");
                       OxyPlugin.debug("Items Effected: " + nextEvent.getAffects().size() + "[" + Arrays.toString(nextEvent.getAffects().toArray()));
                   }
                }

            }
        }
    }

