package fr.asvadia.api.bungee.config;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;


public class YMLConfig {

    Configuration configuration;

    File file;
    String resource;

    Plugin plugin;

    public YMLConfig(String folder, String name, Plugin plugin) {
        this(folder, name, name, plugin);
    }


    public YMLConfig(String folder, String name, String resource, Plugin plugin) {
        this.resource = resource + ".yml";
        this.file = new File(folder + "/" + name + ".yml");
        this.plugin = plugin;
        load();
    }

    public void load() {
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                InputStream in = plugin.getResourceAsStream(resource);
                if(in != null) Files.copy(in, file.toPath());
                else file.createNewFile();
            }
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, this.file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Configuration getResource() {
        return this.configuration;
    }

    public File getFile() {
        return this.file;
    }

    public <T> T get(String where) {
        return get(where, true);
    }

    public <T> T get(String where, boolean color) {
        if(where != null) {
            try {
                Object data = configuration.get(where);
                if (color) {
                    if (data instanceof List) {
                        if (((List<?>) data).size() > 0 && ((List<?>) data).get(0) instanceof String)
                            ((List<String>) data).replaceAll(s -> ChatColor.translateAlternateColorCodes('&', s));
                    } else if (data instanceof String) data = ChatColor.translateAlternateColorCodes('&', (String) data);
                }
                return (T) data;
            } catch(Exception ignored) {}
        }
        return null;
    }

}
