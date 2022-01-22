package fr.asvadia.api.bukkit.messaging.event;

import fr.asvadia.api.bukkit.messaging.request.ReceiveRequest;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public class RequestEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    ReceiveRequest request;

    public RequestEvent(ReceiveRequest  request) {
        this.request = request;
    }

    public ReceiveRequest  getRequest() {
        return request;
    }

    public List<String> getContent() {
        return request.getContent();
    }

    public Player getPlayer() {
        return request.getPlayer();
    }


    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
