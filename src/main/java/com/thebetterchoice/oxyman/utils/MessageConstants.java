package com.thebetterchoice.oxyman.utils;

import com.thebetterchoice.oxyman.OxyPlugin;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

public class MessageConstants {
    @Getter
    private final String EVENT_DISPLAY;
    @Getter
    private final String SHOP_CLOSE;
    @Getter
    private final String SALE_LORE;
    @Getter
    private final String REFRESH_ERROR;
    @Getter
    private final String BONUS_START;
    @Getter
    private final String BONUS_RESET;
    @Getter
    private final String BONUS_END;
    @Getter
    private final String BONUS_ERROR;
    @Getter
    private final String PREFIX;
    @Getter
    private final String BONUS_TOTAL_SPECIFIC;
    @Getter
    private final String BONUS_TOTAL_ALL;
    @Getter
    private final String RELOAD;
    @Getter
    private final String DEBUG_SELL;
    @Getter
    private final String EVENT_MODIFIED_PLUS;
    @Getter
    private final String EVENT_MODIFIED_MINUS;
    @Getter
    private final String SELL_EMPTY;
    @Getter
    private final String SELL;
    @Getter
    private final String SELL_CANCEL;
    @Getter
    private final String SELL_CANCEL_DROP;
    @Getter
    private final String LORE_FILLER;
    @Getter
    private final String LORE_HIGH;
    @Getter
    private final String LORE_LOW;
    @Getter
    private final String GLOBAL_BONUS_END;
    @Getter
    private final String LORE_REFRESH;
    @Getter
    private final String CURRENCY;
    @Getter
    private final String MENU_EVENT_ACTIVE;
    @Getter
    private final String LEAVE_REMOVE_BONUS;
    @Getter
    private final String DECIMAL;
    @Getter
    private final String JOIN_MESSAGE;
    @Getter
    private final String COMMAND_OPEN_A;
    @Getter
    private final String COMMAND_OPEN_B;
    @Getter
    private final String COMMAND_OPEN_C;
    @Getter
    private final String COMMAND_OPEN_D;
    @Getter
    private final String COMMAND_OPEN_E;
    @Getter
    private final String COMMAND_OPEN_F;
    @Getter
    private final String LEAVE_MESSAGE;
    @Getter
    private final String JOIN_ADD_PERMISSION;
    @Getter
    private final String URGENT;
    @Getter
    private final String SELL_LOW_PRICE_WARNING;
    @Getter
    private final String INFLUENCE_COMMAND;
    @Getter
    private final String SHOP_OPEN;
    @Getter
    private final String SHOP_ERROR;
    @Getter
    private final String SELL_ITEM_NOT_FOUND;
    @Getter
    private final String DISPLAY_PRICE_TOP;
    @Getter
    private final String DISPLAY_PRICE_TOP_PERCENTAGE;
    @Getter
    private final String PERSONAL_BOOST_START;
    @Getter
    private final String PERSONAL_BONUS_END;
    @Getter
    private final String GLOBAL_BONUS_EMPTY;
    @Getter
    private final String PERSONAL_BONUS_EMPTY;
    @Getter
    private final String GLOBAL_BONUS_TOTAL;
    @Getter
    private final String EVENT_END;
    @Getter
    private final String MAX_ACTIVE_BONUSES_ERROR;

    public static MessageConstants get() {
        return new MessageConstants();
    }

    public MessageConstants() {
//        if (m.getKeys(false).isEmpty() || !m.getKeys(false).contains("sell") || !m.getKeys(false).contains("bonus")){
//            m.set("personal-bonus-end", "%bonus% % personal boost expires in %formatted_time%");
//            m.set("personal-bonus-start", "Starting personal boost of %bonus% % for %formatted_time%");
//            m.set("bonus-start", "&aStarting bonus of %bonus% for %formatted_time%");
//            m.set("bonus-end","&cEnding bonus of bonus value: %bonus%.");
//            m.set("bonus-error","&4Invalid Bonus Error");
//            m.set("bonus-reset","Your bonus of %bonus% was reset.");
//            m.set("prefix","&aOxy");
//
//            m.set("bonus-specific-total","&aYour total bonus is %bonus% for %time%");
//            m.set("bonus-all-total","&aYou have %amount_of_bonuses% enabled.");
//            m.set("reload","&aPlugin OxyPlugin reloaded!");
//
//            m.set("sell.cancel.basic","&cCancelled Sale of Substances");
//            m.set("refresh-error","&cYou can't refresh that frequently!");
//            m.set("sell.cancel.drop","&cSale cancelled! Your items have been dropped!");
//            m.set("sell.debug","&aItem Sold");
//            m.set("sell.empty","&cNo Items Found");
//            m.set("sell.success"," You sold %bonus%  worth of drugs");
//            m.set("event-increase","&aYou gained %value% !");
//            m.set("event-decrease","&aYou lost %value% !");
//            m.set("cancel-sale","&cCancelled Sale of Substances");
//            m.set("display-price","&ePrice: %bonus%");
//            m.set("bonus-end-global","Global Bonus of %bonus% has ended");
//            m.set("debug.permissions.empty","&cNo Permissions found for %player_name%");
//            m.set("navigation.filler","Empty Slot" );
//            m.set("navigation.high","&aHigh to Low");
//            m.set("navigation.low","&aLow to High");
//            m.set("navigation.refresh","&eClick to Refresh!");
//            m.set("navigation.active.event","&aCurrently there is an active event left for %time%");
//            m.set("join-message","&eWelcome to _Server_Name_");
//            m.set("join-permission","&aYou have received the permission bonus of %bonus%" );
//            m.set("leave-remove","&cYou have lost the %bonus% bonus ! !");
//            m.set("leave-message","&cSee you next time!");
//            m.set("sell.noitem","&cNo Item has been found to sell!");
//            m.set("sell.price.display","&ePrice: ");
//            m.set("sell.price.alert","&cIf you sell now you will lose %currency% %bonus% due to an event! Do /sellall to confirm!");
//            m.set("command.influence","Influenced %drug% from %base% to %modifier%");
//            m.set("shop.close","&c");
//            m.set("shop.open","&a");
//            m.set("shop.error","&4");
//            messagesConfig.save();

        ConfigFile messagesConfig = OxyPlugin.getMessagesConfig();
        FileConfiguration m = messagesConfig.getConfig();
        GLOBAL_BONUS_END = m.getString("bonus-global-end", "Global Bonus of %bonus% has ended");
        GLOBAL_BONUS_EMPTY = m.getString("bonus-global-empty", "No Global Bonuses have been found");
        GLOBAL_BONUS_TOTAL = m.getString("bonus-global-total", "You have a Global bonus amountOfItems of %bonus% remaining for %formatted_time% " );

        PERSONAL_BOOST_START = messagesConfig.getConfig().getString("personal-bonus-start", "Starting personal boost of %bonus% % for %formatted_time%");
        PERSONAL_BONUS_EMPTY = m.getString("personal-bonus-empty", "No Personal Bonuses Found");
        PERSONAL_BONUS_END = messagesConfig.getConfig().getString("personal-bonus-end", "%bonus%% personal boost expires in %formatted_time%");
        BONUS_RESET = m.getString("bonus-reset", "Your bonus of %bonus% was reset.");
        BONUS_START = m.getString("bonus-start", "&aStarting bonus for %formatted_time% of %bonus%");
        BONUS_END = m.getString("bonus-end", "&cEnding bonus of bonus value: %bonus%.");
        BONUS_ERROR = m.getString("bonus-error", "&4Invalid Bonus Error");
        PREFIX = m.getString("prefix", "&aOxyPlugin") + " ";
        BONUS_TOTAL_SPECIFIC = m.getString("bonus-personal-total","You have %bonus% of bonuses active.");
        BONUS_TOTAL_ALL = m.getString("bonus-all-total","%amountOfBonuses% bonuses found.");
        RELOAD = m.getString("reload", "&aPlugin Reloaded");
        REFRESH_ERROR = m.getString("refresh-error", "&cYou can't refresh that frequently!");
        DEBUG_SELL = m.getString("sell.debug", "&aItem Sold");
        SELL_ITEM_NOT_FOUND = m.getString("sell-cancel-none", "&cNo Item has been found to sell!");
        SELL_LOW_PRICE_WARNING = m.getString("sell-alert", "&cIf you sell now you will lose %currency% %value% due to an event! Do /sellall to confirm!");
        SELL_EMPTY = messagesConfig.getConfig().getString("sell.empty", "&cNo Items Found");
        SELL_CANCEL = m.getString("sell-cancel-basic", "&cCancelled Sale of Substances");
        SELL_CANCEL_DROP = m.getString("sell-cancel-drop", "&cSale cancelled! Your items have been dropped!");
        SELL = m.getString("sell-success", " You sold %value%  worth of drugs.");
        EVENT_MODIFIED_PLUS = m.getString("event-increase", "&aYou gained %value% !");
        EVENT_MODIFIED_MINUS = m.getString("event-decrease", "&aYou lost %value% !");
        EVENT_DISPLAY = m.getString("event-display", "%prefix% Event Occurring %display% " + "%affects%").replace("|", " ");
        EVENT_END = m.getString("event-end","Event %event% is ending in %formatted_time%");
        SALE_LORE = m.getString("price-display", "Sale Price: %bonus%");
        LORE_FILLER = m.getString("navigation.filler", "Empty Slot");
        LORE_HIGH = m.getString("navigation.high", "&aHigh to Low");
        LORE_LOW = m.getString("navigation.low", "&aLow to High");
        LORE_REFRESH = m.getString("navigation.refresh", "&eClick to Refresh!");
        MENU_EVENT_ACTIVE = m.getString("navigation.active.event", "&aCurrently there is an active event left for %formatted_time%");
        CURRENCY = m.getString("currency", "$");
        DECIMAL = m.getString("decimal", "#,###.00");
        JOIN_MESSAGE = m.getString("join-message", "&eWelcome to _Server_Name_");
//        JOIN_ADD_BONUS = m.getString("join.bonus","You have received the %bonus% bonus for joining !");
        JOIN_ADD_PERMISSION = m.getString("join-permission", "&aYou have received the permission bonus of %bonus%");
        LEAVE_REMOVE_BONUS = m.getString("leave-remove", "&cYou have lost the %bonus% bonus ! !");
        LEAVE_MESSAGE = m.getString("leave-message", "&cSee you next time!");
        COMMAND_OPEN_A = null;
        COMMAND_OPEN_B = null;
        COMMAND_OPEN_C = null;
        COMMAND_OPEN_E = null;
        COMMAND_OPEN_D = null;
        COMMAND_OPEN_F = null;
        DISPLAY_PRICE_TOP_PERCENTAGE = m.getString("display.percentage", "&a%value% \\%");
        DISPLAY_PRICE_TOP = m.getString("display-price", "&ePrice: ");
        URGENT = m.getString("oxy.join.error", "&4Error! - Please contact an admin!");
        INFLUENCE_COMMAND = m.getString("command.influence", "Influenced %drug% from %base% to %modifier%");
        SHOP_CLOSE = m.getString("shop.close", "&c");
        SHOP_OPEN = m.getString("shop.open", "&a");
        SHOP_ERROR = m.getString("shop.error", "&4");
        MAX_ACTIVE_BONUSES_ERROR = OxyPlugin.getFileConfiguration().getString("Special.Bonuses.Active.Max","&4You have reached the max amount of active bonuses for this server: %bonus_amount%" );
    }

}