package com.thebetterchoice.oxyman.utils;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultSupport {
    @Getter
    private final Economy economy;

    public VaultSupport() {
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
            this.economy = economyProvider != null ? economyProvider.getProvider() : null;
        }
        else {
            throw new RuntimeException("Plugin for Vault is Missing!");
        }
    }
}
