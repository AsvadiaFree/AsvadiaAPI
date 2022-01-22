package fr.asvadia.api.bukkit;

import fr.asvadia.api.bukkit.config.YMLConfig;
import fr.asvadia.api.bukkit.messaging.RequestManager;
import fr.asvadia.api.bukkit.messaging.listener.MessageListener;
import fr.asvadia.api.common.security.Totp;
import net.md_5.bungee.api.ProxyServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class AsvadiaAPI extends JavaPlugin {

    static AsvadiaAPI instance;

    YMLConfig config;

    MessageListener messageListener;

    HashMap<String, RequestManager> requestManagers;

    @Override
    public void onEnable() {
        instance = this;
        this.requestManagers = new HashMap<>();
        this.messageListener = new MessageListener();
    }

    @Override
    public void onDisable() {

    }

    public void registerRequestManager(RequestManager requestManager) {
        requestManagers.put(requestManager.getChannel(), requestManager);
        Bukkit.getMessenger().registerIncomingPluginChannel(this, requestManager.getChannel(), messageListener);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, requestManager.getChannel());
    }

    public HashMap<String, RequestManager> getRequestManagers() {
        return requestManagers;
    }

    public RequestManager getRequestManager(String channel) {
        return requestManagers.get(channel);
    }

    public MessageListener getMessageListener() {
        return messageListener;
    }
    public static AsvadiaAPI getInstance() {
        return instance;
    }
}
