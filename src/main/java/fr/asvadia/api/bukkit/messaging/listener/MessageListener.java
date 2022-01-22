package fr.asvadia.api.bukkit.messaging.listener;

import com.google.common.io.ByteStreams;
import fr.asvadia.api.bukkit.AsvadiaAPI;
import fr.asvadia.api.bukkit.messaging.RequestManager;
import fr.asvadia.api.bukkit.messaging.event.RequestEvent;
import fr.asvadia.api.bukkit.messaging.request.ReceiveRequest;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MessageListener implements PluginMessageListener {


    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if (AsvadiaAPI.getInstance().getRequestManagers().containsKey(channel)) {
            RequestManager requestManager = AsvadiaAPI.getInstance().getRequestManager(channel);
            Map.Entry<UUID, List<String>> result = requestManager.translate(ByteStreams.newDataInput(bytes));
            if (result != null) {
                ReceiveRequest request = new ReceiveRequest(result.getKey(), player, result.getValue());
                requestManager.register(request);
                Bukkit.getPluginManager().callEvent(new RequestEvent(request));
            }
        }
    }

}
