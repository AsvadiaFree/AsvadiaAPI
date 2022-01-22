package fr.asvadia.api.bukkit.reflection;

import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Title {

    List<Object> packets = new ArrayList<>();

    public Title(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        try {
            Constructor<?> packetPlayOutTitleConstructor = Reflector.packetPlayOutTitleClass.getConstructor(Reflector.enumTitleActionClass, Reflector.iChatBaseComponentClass);

            packets.add(Reflector.packetPlayOutTitleClass.getConstructor(int.class, int.class, int.class).newInstance(
                    fadeIn, stay, fadeOut));
            if(title != null) packets.add(packetPlayOutTitleConstructor.newInstance(
                    Reflector.enumTitleActionClass.getField("TITLE").get(null),
                    Reflector.chatSerializerClass.getMethod("a", String.class).invoke(null, "{\"text\": \"" + title + "\"}")));
            if(subtitle != null) packets.add(packetPlayOutTitleConstructor.newInstance(
                    Reflector.enumTitleActionClass.getField("SUBTITLE").get(null),
                    Reflector.chatSerializerClass.getMethod("a", String.class).invoke(null, "{\"text\": \"" + subtitle + "\"}")));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchFieldException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public List<Object> getPackets() {
        return packets;
    }

    public void send(Player player) {
        Reflector.sendPackets(player, packets);
    }
}