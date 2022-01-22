package fr.asvadia.api.bukkit.config;

import com.google.common.base.Charsets;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class YMLConfig {

        FileConfiguration configuration;

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

                    InputStream in = plugin.getResource(resource);
                    if(in != null) Files.copy(in, file.toPath());
                    else file.createNewFile();
                }
                configuration = new YamlConfiguration();
                configuration.load(new InputStreamReader(new FileInputStream(file), Charsets.UTF_8));
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }

        public void save() {
            try {
                configuration.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public FileConfiguration getResource() {
            return configuration;
        }

        public File getFile() {
            return file;
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

