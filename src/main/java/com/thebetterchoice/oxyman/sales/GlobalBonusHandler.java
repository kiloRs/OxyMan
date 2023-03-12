package com.thebetterchoice.oxyman.sales;

import com.thebetterchoice.oxyman.OxyPlugin;
import com.thebetterchoice.oxyman.utils.MessageConstants;
import io.lumine.mythic.lib.MythicLib;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class GlobalBonusHandler {
    private final List<Bonus> boosts = new ArrayList<>();
    private final List<BoostEnder> tasks = new ArrayList<>();
    private double bonus;
    private BoostEnder current = null;
    private World world;

    public GlobalBonusHandler() {
        bonus = OxyPlugin.getPlugin().getConfig().getDouble("Global.Base",0);
    }

    public void reset() {
        this.bonus = 0.0D;

        for (BoostEnder t : this.tasks) {
            t.cancel();
        }

        this.tasks.clear();
        this.boosts.clear();
    }

    public String getFormattedTime() {
        return this.current == null ? null : this.current.getFormattedTime();
    }

    public void addBoost(Bonus toadd) {
            this.startBoost(toadd);
        this.boosts.add(toadd);
    }

    private void startBoost(Bonus boost) {
       // Bukkit.broadcastMessage(var10000 + ChatColor.GRAY + " A " + ChatColor.RED + (int)(boost.getAmountOfItems() * 100.0D) + "%" + ChatColor.GRAY + " global booster is now active for the next " + ChatColor.RED + boost.getFormattedTime() + " (HH:MM)");
        Bukkit.broadcastMessage(MythicLib.plugin.parseColors(MessageConstants.get().getBONUS_START()).replace("%bonus%",(boost.getAmount()<1?boost.getAmount():+boost.getAmount()) * 100.00D + "").replace("%formatted_time%",boost.getFormattedTime() + ""));
        this.bonus += boost.getAmount();
        this.tasks.add(new BoostEnder(boost));
    }

    public double getCurrentBonus() {
        return bonus;
    }

    public class BoostEnder extends BukkitRunnable {
        private final Bonus boost;
        private final long startTime;

        public BoostEnder(Bonus boost) {
            GlobalBonusHandler.this.current = this;
            this.boost = boost;
            this.startTime = System.currentTimeMillis();
            this.runTaskLater(OxyPlugin.getPlugin(), (long)(1200 * boost.getLength()));
        }

        public String getFormattedTime() {
            int left = (int)((System.currentTimeMillis() - this.startTime) / 60000L);
            left = Math.toIntExact(this.boost.getLength() - left);
            int hours = left / 60;
            int min = left % 60;
            String var10000 = String.format("%02d", hours);
            return var10000 + ":" + String.format("%02d", min) + "(HH:MM)";
        }

        public void run() {
            //todo Finish Configuration for Messages here!
            Bukkit.broadcastMessage(MythicLib.plugin.parseColors(MessageConstants.get().getGLOBAL_BONUS_END().replace("%bonus%",boost.getAmount() + "")));
            GlobalBonusHandler.this.bonus -= this.boost.getAmount();
            if (GlobalBonusHandler.this.bonus < 0.0D) {
                GlobalBonusHandler.this.bonus = 0.0D;
            }

            GlobalBonusHandler.this.tasks.remove(this);
            GlobalBonusHandler.this.boosts.remove(this.boost);
            GlobalBonusHandler.this.current = null;
            if (GlobalBonusHandler.this.boosts.size() > 0) {
                GlobalBonusHandler.this.startBoost( GlobalBonusHandler.this.boosts.get(0));
            }

        }
    }
}