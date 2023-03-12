package com.thebetterchoice.oxyman;

import com.thebetterchoice.oxyman.utils.ConfigReference;
import com.thebetterchoice.oxyman.utils.OxyLogger;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class PermissionLoader implements Listener {
    @Getter
    private static final List<PermissionInstance> permissionsUsed = ConfigReference.getPermissions();

    public PermissionLoader(){
        loadRepeat();
    }

    public void loadRepeat(){
        new BukkitRunnable(){
            @Override
            public void run() {
                loadPermissions();
            }
        }.runTask(OxyPlugin.getPlugin());
    }
    private void loadPermissions() {
        for (String eachPermission : OxyPlugin.getMultipliers().getConfig().getKeys(false)) {
            PermissionInstance permissionInstance = new PermissionInstance(eachPermission);
            permissionsUsed.add(permissionInstance);
        }
        OxyLogger.debug("Loading All Permissions Complete!");
    }

    public static boolean hasPermission(String i){
        return permissionsUsed.contains(new PermissionInstance(i));
    }

    public double getValueOfPermission(PermissionInstance i){
        if (!OxyPlugin.getMultipliers().getConfig().isConfigurationSection(i.getPermName())){
            throw new RuntimeException("Permission Config not Located: " + i.getPermName());
        }
        double value = OxyPlugin.getMultipliers().getConfig().getDouble(i.getPermName() + ".value");
        if (!OxyPlugin.getMultipliers().getConfig().isDouble(i.getPermName() + ".value") || value == 0){
            OxyPlugin.log("No Value for Perm: " + i.getPermName());
            return 0;
        }
        return i.getValue();
    }
    public boolean doesPlayerHave(PermissionInstance i, Player player){
        if (!OxyPlugin.getMultipliers().getConfig().isConfigurationSection(i.getPermName())){
            OxyLogger.sendError("No Permission Found: " + i.getPermName());
            return false;
        }
        double value = OxyPlugin.getMultipliers().getConfig().getDouble(i.getPermName() + ".value");
        if (!OxyPlugin.getMultipliers().getConfig().isDouble(i.getPermName() + ".value") || value == 0){
            OxyLogger.sendError("No Permission Value: " + i.getPermName() +  "!");
            return false;
        }
        return player.hasPermission(i.getFullPerm());
    }


    public double getTotalPermissions(Player player){
        OxyUser stored = OxyUser.getStored(player);
        double total = stored.getPermValues();

        OxyPlugin.log("Total Found for Player " + player.getDisplayName() + " is " + total);
        return total;
    }

    public void reload() {
        loadPermissions();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();

        if (permissionsUsed.isEmpty()){
            OxyPlugin.log("No Permissions to Load for Player: " + player.getName() + " because none are in use.");
            return;
        }

        for (PermissionInstance permissionInstance : permissionsUsed) {
            if (!doesPlayerHave(permissionInstance, player)){
                if (player.hasPermission(permissionInstance.getFullPerm())){
                    player.sendRawMessage("Permission Found Although it has errored: " + permissionInstance.getFullPerm());
                }
                continue;
            }
            player.sendRawMessage("Permission Found for" + player.getName() + ": " + permissionInstance.getPermName() + " " + permissionInstance.getFullPerm());
        }

    }
}
