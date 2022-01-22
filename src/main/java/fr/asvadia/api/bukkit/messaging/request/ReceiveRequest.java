package fr.asvadia.api.bukkit.messaging.request;

import fr.asvadia.api.bukkit.messaging.RequestManager;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class ReceiveRequest extends Request {

    List<String> content;

    public ReceiveRequest(UUID id, Player player, List<String> content) {
        super(id, player);
        this.content = content;
    }

    public List<String> getContent() {
        return content;
    }

}