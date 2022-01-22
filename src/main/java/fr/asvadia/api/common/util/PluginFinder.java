package fr.asvadia.api.common.util;

import net.md_5.bungee.api.ProxyServer;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PluginFinder {

    public static Object here() {
        return find(0);
    }

    public static Object last() {
        return find(1);
    }

    public static Object find(int value) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        Class<?> clazz;
        int hashSourceCode;

        if (stackTraceElements.length < 4+value) return null;
        try {
            clazz = Class.forName(stackTraceElements[3+value].getClassName());
        } catch (ClassNotFoundException e) {
            return null;
        }
        hashSourceCode = clazz.getProtectionDomain().getCodeSource().hashCode();

        List<Object> plugins;
        try {
            plugins = new ArrayList<>(Arrays.asList(Bukkit.getPluginManager().getPlugins()));
        } catch (NoClassDefFoundError e) {
            plugins = new ArrayList<>(ProxyServer.getInstance().getPluginManager().getPlugins());
        }

        for (Object plugin : plugins) {
            if (plugin.getClass().getProtectionDomain().getCodeSource().hashCode() == hashSourceCode)
                return plugin;
        }
        return null;
    }

}
