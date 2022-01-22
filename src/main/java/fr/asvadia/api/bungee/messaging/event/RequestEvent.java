package fr.asvadia.api.bungee.messaging.event;

import fr.asvadia.api.bungee.messaging.request.ReceiveRequest;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

import java.util.List;

public class RequestEvent extends Event {

        ReceiveRequest request;

        public RequestEvent(ReceiveRequest request) {
            this.request = request;
        }

        public ReceiveRequest getRequest() {
            return request;
        }

        public List<String> getContent() {
            return request.getContent();
        }

        public ProxiedPlayer getPlayer() {
            return request.getPlayer();
        }

}
