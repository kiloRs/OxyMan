package com.thebetterchoice.oxyman.drugs;

import com.jeff_media.jefflib.NumberUtils;
import com.thebetterchoice.oxyman.OxyPlugin;
import com.thebetterchoice.oxyman.Stats;
import lombok.Getter;
import net.Indyuce.mmoitems.api.item.mmoitem.LiveMMOItem;
import net.Indyuce.mmoitems.stat.data.StringData;
import org.bukkit.inventory.ItemStack;

public class VoucherItem {
    @Getter
    private final double defaultValue;
    @Getter
    private final String name;
    @Getter
    private final long length;
    @Getter
    private final ItemStack item;
    @Getter
    private final BonusType bonusType;
    @Getter
    private final ItemStack itemStack;
    private double savedItemValue = 0;

    public VoucherItem(BonusType bonusType, ItemStack itemStack){
        this.bonusType = bonusType;
        this.itemStack = itemStack;
        this.defaultValue = bonusType==BonusType.NONE?0: bonusType.getValuePercent();
        this.length = bonusType==BonusType.NONE?0: bonusType.getLength();
        this.name = bonusType==BonusType.NONE?null:bonusType.getPublicName();
        this.item = itemStack;
        LiveMMOItem live = new LiveMMOItem(itemStack);
        String word = ((StringData) live.getData(Stats.bonusType)).getString();
        if (live.hasData(Stats.bonusInstance) && word != null && word.contains(" ")) {
            Integer integer = NumberUtils.parseInteger(word.split(" ")[1]);
            if (integer != null){
                this.savedItemValue = integer;
                return;
            }
        }
        this.savedItemValue = defaultValue;
    }

    public double getActualValue(){
        return savedItemValue== defaultValue ? defaultValue :savedItemValue;
    }

    public enum BonusType{
        LOW(OxyPlugin.getFileConfiguration().getDouble("Voucher.Low.Value",0.1),OxyPlugin.getFileConfiguration().getString("Voucher.Low.Name","Low"),OxyPlugin.getFileConfiguration().getLong("Voucher.Low.Length",60)),BASIC(OxyPlugin.getFileConfiguration().getDouble("Voucher.Basic.Value",0.2), OxyPlugin.getFileConfiguration().getString("Voucher.Basic.Name","Basic"),OxyPlugin.getFileConfiguration().getLong("Voucher.Basic.Length",60)),MEDIUM(OxyPlugin.getFileConfiguration().getDouble("Voucher.Medium.Value",0.25),OxyPlugin.getFileConfiguration().getString("Voucher.Medium.Name"),OxyPlugin.getFileConfiguration().getLong("Voucher.Medium.Length",60)),ADVANCED(OxyPlugin.getFileConfiguration().getDouble("Voucher.Advanced.Value",0.3),OxyPlugin.getFileConfiguration().getString("Voucher.Advanced.Name","Advanced"),OxyPlugin.getFileConfiguration().getLong("Voucher.Advanced.Length",60)),EXTREME(OxyPlugin.getFileConfiguration().getDouble("Voucher.Medium.Value",0.5),OxyPlugin.getFileConfiguration().getString("Voucher.Extreme.Name","Extreme"),OxyPlugin.getFileConfiguration().getLong("Voucher.Medium.Length",60)),NONE(0.0,"",0);

        @Getter
        private final Double valuePercent;
        @Getter
        private final String publicName;
        @Getter
        private final long length;

        BonusType(Double v, String publicName, long length){
            valuePercent = v;
            this.publicName = publicName;
            this.length = length;
        }
        public void setLength(long newLength){
            OxyPlugin.getFileConfiguration().set("Voucher." + this.name() + ".Length",newLength);
            OxyPlugin.getPlugin().saveConfig();
        }
    }
}
