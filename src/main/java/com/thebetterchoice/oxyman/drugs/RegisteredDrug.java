package com.thebetterchoice.oxyman.drugs;

import com.thebetterchoice.oxyman.OxyPlugin;
import com.thebetterchoice.oxyman.Stats;
import com.thebetterchoice.oxyman.utils.ConfigReference;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.template.MMOItemTemplate;
import net.Indyuce.mmoitems.stat.data.DoubleData;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisteredDrug extends DrugId {
    private ConfigurationSection configurationSection;
    private double currentPrice = 0;
    private boolean registered = false;

    public RegisteredDrug(BasicDrug basicDrug){
        super(basicDrug.getDrugId().getMmoID());
    }

    /**
     * @param mmoItem Name of the drug to register!
     *
     *           Note: Only instance this for "LoadingDrug" types.
     */
    public RegisteredDrug(@NotNull MMOItemTemplate mmoItem) {
        //Must be done AFTER LoadingDrug is registered!
        super(mmoItem);
        configurationSection = OxyPlugin.getDrugsConfiguration().getConfig().getConfigurationSection(this.getPath());
        if (configurationSection == null) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("Price.Max", ConfigReference.serverMaxCost);
            map.put("Price.Min", ConfigReference.serverMinCost);
            map.put("Price.Base", ConfigReference.serverBaseCost);
            map.put("Demand.Base", ConfigReference.serverDemandBase);
            map.put("Demand.Max", ConfigReference.serverDemandMax);
            map.put("Demand.Min", ConfigReference.serverDemandMin);
//            map.put("Demand.Chance", ConfigReference.demandChance);
            map.put("Type", new ArrayList<String>());

            configurationSection = OxyPlugin.getDrugsConfiguration().getConfig().createSection(this.getPath(), map);
        }

        //Find Current Price?

        currentPrice = ConfigReference.serverBaseCost;
        registered = true;

    }

    public RegisteredDrug(String naming) {
        super(naming);

        configurationSection = OxyPlugin.getDrugsConfiguration().getConfig().getConfigurationSection(this.getPath());
        if (configurationSection==null){
            HashMap<String, Object> map = new HashMap<>();
            map.put("Price.Max",ConfigReference.serverMaxCost);
            map.put("Price.Min",ConfigReference.serverMinCost);
            map.put("Price.Base",ConfigReference.serverBaseCost);
            map.put("Demand.Base",ConfigReference.serverDemandBase);
            map.put("Demand.Max", ConfigReference.serverDemandMax);
            map.put("Demand.Min",ConfigReference.serverDemandMin);
            map.put("Demand.Chance",ConfigReference.demandChance);
            map.put("Type",new ArrayList<String>());

            configurationSection = OxyPlugin.getDrugsConfiguration().getConfig().createSection(this.getPath(), map);
        }
        //Find Current Price?
        currentPrice = ConfigReference.serverBaseCost;

        if (configurationSection == null){
            throw new RuntimeException("Invalid Registered LoadingDrug");
        }
    }

    @Override
    public double getCurrentCost() {
        if (currentPrice>getMaxValue()){
            currentPrice = getMaxValue();
        }
        else if (currentPrice<getMinValue()){
            currentPrice = getMinValue();
        }
        else if (currentPrice == 0){
            return 0;
        }

        return currentPrice;
    }

//    public double getMaxRandomDemand(){
//        return configurationSection.getDouble("Demand.Max",-1);
//    }
//    public double getMinRandomDemand(){
//        return configurationSection.getDouble("Demand.Min",-5);
//    }

     @Override
     public double getMaxValue(){
         return configurationSection.getDouble("Price.Max",10000);
     }
     @Override
     public double getMinValue(){
        return configurationSection.getDouble("Price.Min",0);
     }

//
//    public double getChanceOfDemandIncrease() {
//        return configurationSection.getDouble("Chance.Increase",0.5);
//    }
//    public double getChanceOfDemandDecrease() {
//        return configurationSection.getDouble("Chance.Decrease",0.5);
//    }

    @Override
    public void setValue(int value) {
        setValue(((double) value));
    }

    public void setValue(double value) {
        currentPrice = value;

        if (getItem()==null) {
            throw new RuntimeException("No MMOItem Located!");
        }

        getItem().setData(Stats.currentPrice,new DoubleData(currentPrice));

        if (((DoubleData) getItem().getData(Stats.currentPrice)).getValue() != currentPrice){
            throw new RuntimeException("No Price Matching for Value from SetValue!");
        }


        OxyPlugin.log("Successful Value Set!");
    }

    public BasicDrug getDrug(ItemStack itemStack){
        String id = MMOItems.getID(itemStack);
        if (itemStack == null || id == null||!id.equalsIgnoreCase(mmoID)){
            throw new RuntimeException("Non Matching Drug Item to Registered Drug");
        }
        return new BasicDrug(itemStack);
     }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof RegisteredDrug that)) return false;

        return new EqualsBuilder().appendSuper(super.equals(o)).append(configurationSection.getName(), that.configurationSection.getName()).append(this.mmoID,that.mmoID).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(configurationSection).toHashCode();
    }
}
