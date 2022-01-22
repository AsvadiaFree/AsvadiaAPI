package fr.asvadia.api.bungee.messaging.request;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class Request {

    UUID id;

    ProxiedPlayer player;

    public Request(ProxiedPlayer player) {
        this(UUID.randomUUID(), player);
    }

    public Request(UUID id, ProxiedPlayer player) {
        this.id = id;
        this.player = player;
    }

    public UUID getId() {
        return id;
    }

    public ProxiedPlayer getPlayer() {
        return player;
    }




}
