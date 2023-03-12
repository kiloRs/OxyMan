package com.thebetterchoice.oxyman.drugs;

import com.thebetterchoice.oxyman.Stats;
import lombok.Getter;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.Indyuce.mmoitems.api.item.template.MMOItemTemplate;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;

public abstract class DrugId {
    @Getter
    private final MMOItemTemplate itemTemplate;
    @Getter
    private final MMOItem item;
    @Getter
    public String mmoID;


    public DrugId(MMOItemTemplate template) {
        this(template.getId());

    }

    public DrugId(String mmoID) {
        this.mmoID = mmoID.toUpperCase(Locale.ROOT);
        itemTemplate = MMOItems.plugin.getTemplates().getTemplate(Stats.DRUG,this.mmoID);

        if (itemTemplate == null){
            throw new RuntimeException("No Template Found for " + Stats.DRUG.getId() + "  " + mmoID.toUpperCase(Locale.ROOT));
        }
        item = itemTemplate.newBuilder().build();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DrugId drugId)) return false;
        if (!drugId.item.getType().equals(item.getType())) {
            return false;
        }

        return item.getId().equals(drugId.item.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.mmoID,this.itemTemplate);
    }

    public String getPath() {
        return itemTemplate.getId();
    }


    @NotNull
    public MMOItemTemplate getTemplate(){
        return MMOItems.plugin.getTemplates().getTemplateOrThrow(Stats.DRUG,mmoID);
    }

    public abstract double getCurrentCost();

    public abstract double getMaxValue();

    public abstract double getMinValue();

    public abstract void setValue(int value);


}
