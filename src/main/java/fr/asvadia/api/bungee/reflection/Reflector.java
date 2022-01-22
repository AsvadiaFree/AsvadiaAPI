package fr.asvadia.api.bungee.reflection;

import com.google.common.collect.Multimap;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import java.lang.reflect.Field;

public class Reflector {

    public static Multimap<Plugin, Command> getCommands() {
        try {
            ProxyServer proxyServer = ProxyServer.getInstance();
            Class<?> pluginManager = proxyServer.getPluginManager().getClass();
            Field field = pluginManager.getDeclaredField("commandsByPlugin");
            field.setAccessible(true);
            return (Multimap<Plugin, Command>) field.get(proxyServer.getPluginManager());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

}
