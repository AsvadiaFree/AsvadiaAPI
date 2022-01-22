package fr.asvadia.api.bukkit.util;

import fr.asvadia.api.bukkit.reflection.ActionBar;
import fr.asvadia.api.bukkit.reflection.Reflector;
import fr.asvadia.api.bukkit.reflection.Title;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Creator {

    public static ItemStack item(ConfigurationSection section) {
        String trace = "material";
        try {
            ItemStack item = new ItemStack(Material.getMaterial(section.getString("material").toUpperCase()));
            ItemMeta meta = item.getItemMeta();
            if (section.contains("rgb")) {
                trace = "rgb";
                if (item.getType().toString().contains("LEATHER_")) {
                    String[] rgb = section.getString("rgb").split(", ");
                    ((LeatherArmorMeta) meta).setColor(Color.fromRGB(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2])));
                }
            } if (section.contains("name")) {
                trace = "name";
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', section.getString("name")));
            } if (section.contains("lore")) {
                trace = "lore";
                List<String> lore = new ArrayList<>(section.getStringList("lore"));
                lore.replaceAll(s -> ChatColor.translateAlternateColorCodes('&', s));
                meta.setLore(lore);
            } if (section.contains("attribute")) {
                trace = "attribute";
                ConfigurationSection attributeSection = section.getConfigurationSection("attribute");
                HashMap<String, Double> map = new HashMap<>();
                attributeSection.getKeys(false).forEach(s -> map.put(s.replaceAll(";", "."), attributeSection.getDouble(s)));
                Reflector.setAttributes(item, map);
            } if (section.contains("enchant")) {
                trace = "enchant";
                ConfigurationSection enchantSection = section.getConfigurationSection("enchant");
                enchantSection.getKeys(false).forEach(s -> meta.addEnchant(Enchantment.getByName(s.toUpperCase()), enchantSection.getInt(s), true));
            } if (section.contains("hide")) {
                trace = "hide";
                List<String> hide = section.getStringList("hide");
                if (hide.contains("enchant")) meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                if (hide.contains("attribute")) meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            };
            item.setItemMeta(meta);
            return item;
        } catch (Exception e) {
            System.out.println("Error ItemBuilder : at " + section.getCurrentPath() + "." + trace);
        }
        return new ItemStack(Material.DIRT);
    }

    public static Title title(String title) {
        return title(title, "");
    }

    public static Title title(String title, String subtitle) {
        return title(title, subtitle, 10, 20, 10);
    }

    public static Title title(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        return new Title(title, subtitle, fadeIn, stay, fadeOut);
    }

    public static ActionBar actionBar(String message) {
        return new ActionBar(message);
    }



}
