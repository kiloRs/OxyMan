package com.thebetterchoice.oxyman;

import lombok.Getter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.configuration.ConfigurationSection;

public class PermissionInstance {
    @Getter
    private final String permName;
    @Getter
    private final String fullPerm;
    /**
     * Value of the permission bonus!
     */
    @Getter
    private final Double value;

    /**
     * Prefix to the permission!
     */
    private static final String PERMISSION_PREFIX = "Oxy.Multiplier.";

    /**
     * @param i Last Key Group Name of Permission - Use PERMISSION_PREFIX + i to get full permission.
     */
    public PermissionInstance(String i){
        this.permName = i;
        this.fullPerm = PERMISSION_PREFIX + permName;
//        try {
//            this.group = LuckPermsProvider.get().getGroupManager().createAndLoadGroup(i).get();
//        } catch (InterruptedException | ExecutionException e) {
//            String m = "Invalid LuckPerms Group Load from Oxy: " + i;
//            Bukkit.getConsoleSender().sendMessage(MythicLib.plugin.parseColors(m));
//
//            e.printStackTrace();
//        }
        save();

        this.value = OxyPlugin.getMultipliers().getConfig().getDouble(permName + ".value");
    }

    private void save() {
        if (!OxyPlugin.getMultipliers().getConfig().isConfigurationSection(permName)) {
            OxyPlugin.getMultipliers().getConfig().set(permName + ".value",0.1);
            OxyPlugin.getMultipliers().getConfig().set(permName + ".types",new String[]{});
            OxyPlugin.getMultipliers().save();
        }
    }

    protected int exists() {
        ConfigurationSection configurationSection = OxyPlugin.getMultipliers().getConfig().getConfigurationSection(permName);

        if (!OxyPlugin.getMultipliers().exists() || !OxyPlugin.getMultipliers().getConfig().isConfigurationSection(permName) || configurationSection == null){
            return 0;
        }
        if (!configurationSection.isDouble("values")){
            return 1;
        }
        double v = configurationSection.getDouble("values", 0.1);
        return (v >0|| v <0)?2:1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof PermissionInstance that)) return false;

        return new EqualsBuilder().append(this.getPermName(), that.getPermName()).append(getFullPerm(), that.getFullPerm()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getPermName()).append(getFullPerm()).toHashCode();
    }
}
