package fr.asvadia.api.bukkit.messaging.request;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class Request {

    UUID id;

    Player player;

    public Request(Player player) {
        this(UUID.randomUUID(), player);
    }

    public Request(UUID id, Player player) {
        this.id = id;
        this.player = player;
    }

    public UUID getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

}
