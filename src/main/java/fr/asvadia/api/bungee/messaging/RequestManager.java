package fr.asvadia.api.bungee.messaging;

import com.google.common.io.ByteArrayDataInput;
import fr.asvadia.api.bungee.AsvadiaAPI;
import fr.asvadia.api.bungee.messaging.event.ResponseRequestEvent;
import fr.asvadia.api.bungee.messaging.listener.MessageListener;
import fr.asvadia.api.bungee.messaging.request.ReceiveRequest;
import fr.asvadia.api.bungee.messaging.request.Request;
import fr.asvadia.api.common.security.Totp;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

public class RequestManager {

    HashMap<UUID, ReceiveRequest> receivingRequests = new HashMap<>();

    String channel;

    String key;

    public RequestManager(String channel, String key) {
        this.channel = channel;
        this.key = key;
    }

    public void register(ReceiveRequest request) {
        receivingRequests.put(request.getId(), request);
    }

    public ReceiveRequest getReceivingRequest(UUID id) {
        return receivingRequests.get(id);
    }

    public HashMap<UUID, ReceiveRequest> getReceivingRequests() {
        return receivingRequests;
    }

    public Map.Entry<UUID, List<String>> translate(ByteArrayDataInput in) {
        try {
            final String code = in.readUTF();
            if (code.equals(Totp.getTOTPCode(getKey()))) {
                final UUID id = UUID.fromString(in.readUTF());
                final List<String> content = new ArrayList<>();
                try {
                    while (true) content.add(in.readUTF());
                } catch (Exception e) {
                    return new AbstractMap.SimpleEntry<>(id, content);
                }
            }
        } catch (IllegalStateException ignored) {}
        return null;
    }

    public String getKey() {
        return key;
    }

    public String getChannel() {
        return channel;
    }

    public void request(ProxiedPlayer player, String... contents) {
        request(new Request(player), contents);
    }

    public void request(Request request, String... contents) {
        request(null, request, contents);
    }

    public void request(ResponseRequestEvent event, ProxiedPlayer player, String... contents) {
        request(event, new Request(player), contents);
    }

    public void request(ResponseRequestEvent event, Request request, String... contents) {
        if (contents.length > 0) {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            try {
                dataOutputStream.writeUTF(Totp.getTOTPCode(key));
                dataOutputStream.writeUTF(request.getId().toString());
                for (String content : contents) dataOutputStream.writeUTF(content);
            } catch (IOException e) {
                e.printStackTrace();
            }

            request.getPlayer().getServer().sendData(channel, byteArrayOutputStream.toByteArray());
            if (event != null) {

                receivingRequests.remove(request.getId());
                new Timer().schedule(new TimerTask() {

                    int time;

                    @Override
                    public void run() {
                        time += 5;
                        if (getReceivingRequests().get(request.getId()) != null) {
                            event.call(getReceivingRequest(request.getId()));
                            cancel();
                        } else if (time >= 4000) {
                            event.call(null);
                            cancel();
                        }
                    }
                }, 0, 20);
            }

        } else throw new IllegalArgumentException();
    }

}


