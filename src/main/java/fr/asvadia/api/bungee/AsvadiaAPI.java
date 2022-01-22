package fr.asvadia.api.bungee;

import fr.asvadia.api.bungee.messaging.RequestManager;
import fr.asvadia.api.bungee.config.YMLConfig;
import fr.asvadia.api.bungee.messaging.listener.MessageListener;
import fr.asvadia.api.common.security.Totp;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.Material;

import java.util.HashMap;

public class AsvadiaAPI extends Plugin {

    static AsvadiaAPI instance;

    YMLConfig config;

    MessageListener messageListener;

    HashMap<String, RequestManager> requestManagers;

    @Override
    public void onEnable() {
        instance = this;
        this.requestManagers = new HashMap<>();
        this.messageListener = new MessageListener();

        ProxyServer.getInstance().getPluginManager().registerListener(AsvadiaAPI.getInstance(), messageListener);

    }

    @Override
    public void onDisable() {

    }

    public void registerRequestManager(RequestManager requestManager) {
        requestManagers.put(requestManager.getChannel(), requestManager);
        ProxyServer.getInstance().registerChannel(requestManager.getChannel());
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
