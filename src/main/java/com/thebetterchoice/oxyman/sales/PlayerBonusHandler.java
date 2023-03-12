package com.thebetterchoice.oxyman.sales;

import com.thebetterchoice.oxyman.OxyPlugin;
import com.thebetterchoice.oxyman.OxyUser;
import com.thebetterchoice.oxyman.utils.ConfigReference;
import com.thebetterchoice.oxyman.utils.MessageConstants;
import io.lumine.mythic.lib.MythicLib;
import lombok.Getter;
import net.Indyuce.mmoitems.api.ConfigFile;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PlayerBonusHandler {
    @Getter
    private final List<Bonus> allBonusesOfPlayer = new ArrayList<>();
    public final Map<Bonus,BonusCloser> closerMap = new HashMap<>();

    /**
     * Players Config File - Used
     */
    @Getter
    private final ConfigFile c;
    private double total;
    private final UUID uuid;
    protected Player player;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof PlayerBonusHandler that)) return false;

        return new EqualsBuilder().append(uuid, that.uuid).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(uuid).toHashCode();
    }

    public PlayerBonusHandler(OxyUser user){
        this.total = allBonusesOfPlayer.size();
        this.uuid = user.getPlayer().getUniqueId();
        player = Bukkit.getPlayer(uuid);

        c = user.getConfig();

        if (closerMap.size()!=allBonusesOfPlayer.size()){
            throw new RuntimeException("Invalid Bonus Size [" + closerMap.size() + " - " + allBonusesOfPlayer.size() + "]");
        }

        getBonusAmount();


    }

    public double getTotalBonusAmount(boolean log){
        double bonusAmount = getBonusAmount();

        if (log){
            OxyPlugin.debug("Bonus Found: " + bonusAmount);
        }
        return bonusAmount;
    }
    private double getBonusAmount() {
        total = 0;
        for (Bonus bonus : allBonusesOfPlayer) {
            double amount = bonus.getAmount();
            this.total = amount + this.total;
        }

        return total;
    }

    public PlayerBonusHandler(UUID uuid) {
        this(OxyUser.getStored(uuid));

    }

    public void addBonus(Bonus oxyBonus){
        Iterator<Bonus> var2 = this.getAllBonusesOfPlayer().iterator();

        Bonus b;
        do {
            if (!var2.hasNext()) {
                this.startBoost(oxyBonus);
                return;
            }

            b = var2.next();
        } while(b.getAmount() != oxyBonus.getAmount());

        this.updateBonus(b, oxyBonus);
    }
    public void removeBonus(Bonus bonus){
        closerMap.get(bonus).run();
    }

    public void addBonuses(Bonus[] bonuses){
        for (Bonus bonus : bonuses) {
            addBonus(bonus);
        }
    }
    private void startBoost(Bonus boost) {
        Player player = Bukkit.getPlayer(this.uuid);
        if (player != null) {
            String prefix = MythicLib.plugin.parseColors(MessageConstants.get().getPREFIX());
            player.sendMessage(MythicLib.plugin.parseColors( prefix+" "+ MessageConstants.get().getPERSONAL_BOOST_START().replace("%bonus%",(int)(boost.getAmount() * 100.0D) + "%")).replace("%formatted_time%",boost.getFormattedTime()));

        if (allBonusesOfPlayer.size()>= ConfigReference.maxActiveBonuses){
            String mess = MessageConstants.get().getMAX_ACTIVE_BONUSES_ERROR().replace("%bonus_amount%", allBonusesOfPlayer.size() + "");
            player.sendRawMessage(MythicLib.plugin.parseColors(mess));
            return;
        }

        this.allBonusesOfPlayer.add(boost);
        this.total += boost.getAmount();
        closerMap.put(boost, new BonusCloser(boost));
    }}

    private void updateBonus(Bonus curr, Bonus next) {
        Player player = Bukkit.getPlayer(this.uuid);
        if (player == null){
            throw new RuntimeException("No Player for Bonus found!");
        }

        String prefix = MythicLib.plugin.parseColors(MessageConstants.get().getPREFIX());
        player.sendMessage(MythicLib.plugin.parseColors(prefix+ " " + MessageConstants.get().getPERSONAL_BOOST_START().replace("%bonus%",(int)(next.getAmount() * 100.0D) + "%").replace("%formatted_time%",next.getFormattedTime())));

        BonusCloser task = this.closerMap.get(curr);
        task.cancel();
        long timeLeft = (System.currentTimeMillis() - task.getStartTime()) / 50L;
        timeLeft = (curr.getLength() * 20 * 60) - timeLeft;
        timeLeft = timeLeft + (next.getLength() * 20 * 60);
        curr.setLength(curr.getLength() + next.getLength());
        closerMap.remove(task.oxyBonus);
        closerMap.put(curr, new BonusCloser(curr,timeLeft));
    }

//    public void addPermissionBonus(){
//        player = Bukkit.getPlayer(uuid);
//    }

    public double getCurrentBonus(){
        return total;
    }

    public void setTotal(double baseTotal) {
        this.total = baseTotal;
    }

    public class BonusCloser extends BukkitRunnable {
        private final Bonus oxyBonus;
        private final long startTime;

        public BonusCloser(Bonus bonus) {
            this(bonus, bonus.getLength() * 20 * 60);
        }

        public BonusCloser(Bonus boost, long timeTicks) {
            this.oxyBonus = boost;
            this.startTime = System.currentTimeMillis();

            this.runTaskLater(OxyPlugin.getPlugin(), timeTicks);
        }

        public Bonus getOxyBonus() {
            return this.oxyBonus;
        }

        public long getStartTime() {
            return this.startTime;
        }

        public String getFormattedTime() {
            int left = (int)((System.currentTimeMillis() - this.startTime) / 60000L);
            left = Math.toIntExact(this.oxyBonus.getLength() - left);
            int hours = left / 60;
            int min = left % 60;
            String var10000 = String.format("%02d", hours);
            return var10000 + ":" + String.format("%02d", min) + " (HH:MM)";
        }

        public void run() {
            Player player = OxyUser.getStored(PlayerBonusHandler.this.uuid).getPlayer();

            String prefix = MythicLib.plugin.parseColors(OxyPlugin.getPrefix());
                player.sendMessage(prefix + MythicLib.inst().parseColors(MessageConstants.get().getPERSONAL_BONUS_END().replace("%bonus%","" + (int)(this.oxyBonus.getAmount() * 100.0D) + "%")));


            PlayerBonusHandler handler = PlayerBonusHandler.this;
            handler.total -= this.oxyBonus.getAmount();
            if (handler.total < 0.0D) {
                handler.total = 0.0D;
            }

            closerMap.remove(this.oxyBonus);
            allBonusesOfPlayer.remove(this.oxyBonus);
        }
    }
}
