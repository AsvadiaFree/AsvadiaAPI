package fr.asvadia.api.bukkit.util;

import org.bukkit.Material;

public enum Slot {
    HEAD("head"),
    CHEST("chest"),
    FEET("feet"),
    LEGS("legs"),
    MAIN_HAND("mainhand");

    String key;

    Slot(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static Slot getMaterialSlot(Material material) {
        if(material.toString().contains("HELMET")) return Slot.HEAD;
        else if(material.toString().contains("CHESTPLATE")) return Slot.CHEST;
        else if(material.toString().contains("LEGGINGS")) return Slot.LEGS;
        else if(material.toString().contains("BOOTS")) return Slot.FEET;
        else return Slot.MAIN_HAND;
    }

}
