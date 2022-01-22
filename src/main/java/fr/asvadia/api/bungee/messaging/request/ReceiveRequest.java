package fr.asvadia.api.bungee.messaging.request;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.UUID;

public class ReceiveRequest extends Request {

    List<String> content;

    public ReceiveRequest(UUID id, ProxiedPlayer player, List<String> content) {
        super(id, player);
        this.content = content;
    }

    public List<String> getContent() {
        return content;
    }

}
