package com.thebetterchoice.oxyman;

import com.thebetterchoice.oxyman.events.DrugSellEvent;
import com.thebetterchoice.oxyman.sales.Bonus;
import com.thebetterchoice.oxyman.sales.PlayerBonusHandler;
import com.thebetterchoice.oxyman.utils.ConfigReference;
import com.thebetterchoice.oxyman.utils.OxyLogger;
import lombok.Getter;
import net.Indyuce.mmoitems.api.ConfigFile;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class OxyUser {
    private static final Map<UUID,OxyUser> data = ConfigReference.getData();

    private PlayerBonusHandler playerBonusHandler; //Handler

    @Getter
    private ConfigFile config; //UUID Configuration
    @Getter
    private final Player player; //Player
    @Getter
    private double permValues; //Used for total perm value

    public static OxyUser getStored(Player player){
        return data.get(player.getUniqueId());
    }
    public static OxyUser getStored(UUID uuid){
        return data.get(uuid);
    }
    public OxyUser(Player player){
        this(player.getUniqueId());
    }
    public OxyUser(UUID uuid) {
        data.putIfAbsent(uuid, this);



        this.player = Bukkit.getPlayer(uuid);


        String v = OxyPlugin.format(1);
        OxyPlugin.log("V: "+ v);
        if (config == null) {
            config = new ConfigFile(OxyPlugin.getPlugin(), "/userdata/", uuid.toString());
        }

        if (!config.exists()) {
            config.setup();
        }
        this.playerBonusHandler = new PlayerBonusHandler(this);

        //A list of all active bonuses on player
        List<Bonus> current = playerBonusHandler.getAllBonusesOfPlayer();

        if (current.isEmpty()){
            OxyPlugin.debug("No Bonuses on Player: " + player.getName());
        }


        loadPerms();
    }

    /**
     * @param e Only Use for this event firing, for tracking recent!
     */
    public OxyUser(DrugSellEvent e){
        this(e.getPlayer().getPlayer());

    }


    public void loadPerms(){
        for (PermissionInstance permission : ConfigReference.getPermissions()) {
            if (OxyPlugin.getPermissionLoader().doesPlayerHave(permission,player)){
                permValues += OxyPlugin.getPermissionLoader().getValueOfPermission(permission);
                OxyLogger.log("Permission Loader: " + permValues + " is value of all permissions of " + player.getName() + " including " + permission);
            }
        }
        OxyPlugin.log("Total Value for " + player.getName() + " is " + permValues);
    }


    @NotNull
    public PlayerBonusHandler getBonusHandler(){
        if (playerBonusHandler == null){
            playerBonusHandler = new PlayerBonusHandler(this);
        }

        return playerBonusHandler;
    }

    public void addBonus(Bonus bonus){
        getCurrentBonuses().add(bonus);
    }

    public List<Bonus> getCurrentBonuses() {
        return playerBonusHandler.getAllBonusesOfPlayer();
    }

    public void removeBonus(Bonus bonus){
        getCurrentBonuses().remove(bonus);
    }
    public void clearAllBonuses(){
        getCurrentBonuses().clear();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof OxyUser oxyUser)) return false;

        return new EqualsBuilder().append(getPlayer().getUniqueId(), oxyUser.getPlayer().getUniqueId()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getPlayer()).toHashCode();
    }
}
