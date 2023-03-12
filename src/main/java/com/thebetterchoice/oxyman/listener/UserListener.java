package com.thebetterchoice.oxyman.listener;

import com.thebetterchoice.oxyman.OxyPlugin;
import com.thebetterchoice.oxyman.OxyUser;
import com.thebetterchoice.oxyman.menu.MenuInstance;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class UserListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH,ignoreCancelled = true)
    public void onUserJoin(PlayerJoinEvent e){
        OxyUser o = new OxyUser(e.getPlayer());

        if (!o.getConfig().exists()){
            o.getConfig().setup();
        }

        OxyPlugin.debug("Player: "+e.getPlayer().getName() + " Permissions Values: " + o.getPermValues());


        new BukkitRunnable(){
            @Override
            public void run() {
                e.getPlayer().openInventory(new MenuInstance(e.getPlayer(),false).getInventory());
                e.getPlayer().sendRawMessage("Opening Menu for Testing!");
            }
        }.runTaskLaterAsynchronously(OxyPlugin.getPlugin(),1000);
    }



    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        OxyUser user = OxyUser.getStored(e.getPlayer());
    }
}
