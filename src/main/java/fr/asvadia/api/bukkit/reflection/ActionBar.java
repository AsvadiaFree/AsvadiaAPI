package fr.asvadia.api.bukkit.reflection;

import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class ActionBar {

    Object packet;

    public ActionBar(String message) {
        try {
            if(Reflector.versionNumber > 16.0) {
                packet = Reflector.packetPlayOutChat.getConstructor(Reflector.iChatBaseComponentClass, Reflector.chatMessageType, UUID.class).newInstance(
                        Reflector.chatSerializerClass.getMethod("a", String.class).invoke(null, "{\"text\": \"" + message + "\"}"),
                        Reflector.chatMessageType.getField("GAME_INFO").get(null), UUID.randomUUID());
            } else {
                packet = Reflector.packetPlayOutChat.getConstructor(Reflector.iChatBaseComponentClass, byte.class).newInstance(
                        Reflector.chatSerializerClass.getMethod("a", String.class).invoke(null, "{\"text\": \"" + message + "\"}"),
                        (byte) 2);
            }
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public Object getPacket() {
        return packet;
    }

    public void send(Player player) {
        Reflector.sendPacket(player, packet);
    }
}
