package fr.asvadia.api.bungee.messaging.listener;

import com.google.common.io.ByteStreams;
import fr.asvadia.api.bungee.AsvadiaAPI;
import fr.asvadia.api.bungee.messaging.RequestManager;
import fr.asvadia.api.bungee.messaging.event.RequestEvent;
import fr.asvadia.api.bungee.messaging.request.ReceiveRequest;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MessageListener implements Listener {


    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (AsvadiaAPI.getInstance().getRequestManagers().containsKey(event.getTag())) {
            RequestManager requestManager = AsvadiaAPI.getInstance().getRequestManager(event.getTag());
            Map.Entry<UUID, List<String>> result = requestManager.translate(ByteStreams.newDataInput(event.getData()));
            if (result != null) {
                ReceiveRequest request = new ReceiveRequest(result.getKey(), (ProxiedPlayer) event.getReceiver(), result.getValue());
                requestManager.register(request);
                ProxyServer.getInstance().getPluginManager().callEvent(new RequestEvent(request));
            }
        }
    }



}
