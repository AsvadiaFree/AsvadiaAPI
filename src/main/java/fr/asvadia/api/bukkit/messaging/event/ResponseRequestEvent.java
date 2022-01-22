package fr.asvadia.api.bukkit.messaging.event;

import fr.asvadia.api.bukkit.messaging.request.ReceiveRequest;

public interface ResponseRequestEvent {

    void call(ReceiveRequest request);

}