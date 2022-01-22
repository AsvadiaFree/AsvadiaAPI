package fr.asvadia.api.bungee.util;

import net.md_5.bungee.BungeeTitle;
import net.md_5.bungee.api.chat.TextComponent;

public class Creator {

    public static BungeeTitle title(String title) {
        return title(title, "");
    }

    public static BungeeTitle title(String title, String subtitle) {
        return title(title, subtitle, 10, 20, 10);
    }

    public static BungeeTitle title(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        BungeeTitle packet = new BungeeTitle();
        packet.title(TextComponent.fromLegacyText(title));
        packet.subTitle(TextComponent.fromLegacyText(subtitle));
        packet.fadeIn(fadeIn);
        packet.fadeOut(fadeOut);
        packet.stay(stay);
        return packet;
    }


}
