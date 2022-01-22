package fr.asvadia.api.bungee.messaging.event;

import fr.asvadia.api.bungee.messaging.request.ReceiveRequest;

public interface ResponseRequestEvent {

    void call(ReceiveRequest request);

}