package fr.asvadia.api.bukkit.reflection;

import fr.asvadia.api.bukkit.util.Slot;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Reflector {

    static String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    static double versionNumber = Double.parseDouble(version.replaceFirst("v1_", "").replaceAll("_R", "."));

    static Class<?> iChatBaseComponentClass = getNMSClass("IChatBaseComponent");;
    static Class<?> chatSerializerClass = iChatBaseComponentClass.getDeclaredClasses()[0];

    static Class<?> packetPlayOutTitleClass = getNMSClass("PacketPlayOutTitle");
    static Class<?> enumTitleActionClass = packetPlayOutTitleClass.getDeclaredClasses()[0];

    static Class<?> chatMessageType = getNMSClass("ChatMessageType");

    static Class<?> packetPlayOutChat = getNMSClass("PacketPlayOutChat");;

    static Class<?> itemStackClass = getNMSClass("ItemStack");;

    static Class<?> nbtBase = getNMSClass("NBTBase");;

    static Class<?> nbtTagCompoundClass = getNMSClass("NBTTagCompound");

    static Class<?> nbtTagList = getNMSClass("NBTTagList");

    static Class<?> nbtTagString = getNMSClass("NBTTagString");

    static Class<?> craftItemStack = getCraftBukkitClass("inventory.CraftItemStack");;

    static Class<?> packetClass = getNMSClass("Packet");



    public static Class<?> getNMSClass(String name) {
        return getClass("net.minecraft.server." + version + "." + name);
    }

    public static Class<?> getCraftBukkitClass(String name) {
        return getClass("org.bukkit.craftbukkit." + version + "." + name);
    }

    public static Class<?> getClass(String path) {
        try {
            return Class.forName(path);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static double getVersionNumber() {
        return versionNumber;
    }



    //SEND PACKET
    public static Object getPlayerConnection(Player player) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            return handle.getClass().getField("playerConnection").get(handle);
        } catch (IllegalAccessException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void sendPackets(Player player, List<Object> packets) {
        Object playerConnection = getPlayerConnection(player);
        packets.forEach(packet -> sendPacket(playerConnection, packet));
    }

    public static void sendPacket(Player player, Object packet) {
        sendPacket(getPlayerConnection(player), packet);
    }

    private static void sendPacket(Object playerConnection, Object packet) {
        try {
            playerConnection.getClass().getMethod("sendPacket", packetClass).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void setAttributes(ItemStack item, Map<String, Double> attributes) {
        try {
            Object itemStack = craftItemStack.getMethod("asNMSCopy", ItemStack.class).invoke(null, item);
            Object nbt = ((boolean) itemStackClass.getMethod("hasTag").invoke(itemStack)) ? itemStackClass.getMethod("getTag").invoke(itemStack) : nbtTagCompoundClass.getConstructor().newInstance();
            if (attributes != null) {
                Object list = nbtTagList.getConstructor().newInstance();

                Method setString = nbtTagCompoundClass.getMethod("setString", String.class, String.class);
                Method setDouble = nbtTagCompoundClass.getMethod("setDouble", String.class, double.class);
                Method setInt = nbtTagCompoundClass.getMethod("setInt", String.class, int.class);
                Method setLong = nbtTagCompoundClass.getMethod("setLong", String.class, long.class);
                Method add = list.getClass().getMethod("add", nbtBase);
                for (Map.Entry<String, Double> attribute : attributes.entrySet()) {
                    UUID uuid = UUID.randomUUID();
                    Object compound = nbtTagCompoundClass.getConstructor().newInstance();
                    setString.invoke(compound, "AttributeName", attribute.getKey());
                    setString.invoke(compound, "Name", attribute.getKey());
                    setDouble.invoke(compound, "Amount", attribute.getValue());
                    setInt.invoke(compound, "Operation", 0);
                    setLong.invoke(compound, "UUIDLeast", uuid.getLeastSignificantBits());
                    setLong.invoke(compound, "UUIDMost", uuid.getMostSignificantBits());
                    setString.invoke(compound, "Slot", Slot.getMaterialSlot(item.getType()).getKey());
                    add.invoke(list, compound);
                }
                nbtTagCompoundClass.getMethod("set", String.class, nbtBase).invoke(nbt, "AttributeModifiers", list);
            } else if ((boolean) nbtTagCompoundClass.getMethod("hasKey", String.class).invoke(nbt, "AttributeModifiers"))
                nbtTagCompoundClass.getMethod("remove", String.class).invoke(nbt, "AttributeModifiers");
            itemStackClass.getMethod("setTag", nbtTagCompoundClass).invoke(itemStack, nbt);
            item.setItemMeta(((ItemStack) craftItemStack.getMethod("asBukkitCopy", itemStackClass).invoke(null, itemStack)).getItemMeta());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            e.printStackTrace();
        }
    }


    public static Map<String, Double> getAttributes(ItemStack item) {
        try {
            Object itemStack = craftItemStack.getMethod("asNMSCopy", ItemStack.class).invoke(null, item);
            Object nbt = ((boolean) itemStackClass.getMethod("hasTag").invoke(itemStack)) ? itemStackClass.getMethod("getTag").invoke(itemStack) : nbtTagCompoundClass.getConstructor().newInstance();
            Object list = nbtTagCompoundClass.getMethod("get", String.class).invoke(nbt, "AttributeModifiers");
            if (list != null) {
                Map<String, Double> attributes = new HashMap<>();

                Method getCompound = nbtTagList.getMethod("g", int.class);
                Method getString = nbtTagCompoundClass.getMethod("getString", String.class);
                Method getDouble = nbtTagCompoundClass.getMethod("getDouble", String.class);
                for (int i = 0; i < (int) nbtTagList.getMethod("size").invoke(list); i++) {
                    Object compound = getCompound.invoke(list, i);
                    attributes.put((String) getString.invoke(compound, "AttributeName"), (double) getDouble.invoke(compound, "Amount"));
                }
                return attributes;
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException ignored) {}
        return null;
    }

    //ADD NBT
    public static ItemStack addNbt(ItemStack item, String key, String value) {
        try {
            Object itemStack = craftItemStack.getMethod("asNMSCopy", ItemStack.class).invoke(null, item);
            Object nbt = ((boolean) itemStackClass.getMethod("hasTag").invoke(itemStack)) ? itemStackClass.getMethod("getTag").invoke(itemStack) : nbtTagCompoundClass.getConstructor().newInstance();

            nbtTagCompoundClass.getMethod("set", String.class, nbtBase).invoke(nbt, key, nbtTagString.getConstructor(String.class).newInstance(value));

            itemStackClass.getMethod("setTag", nbtTagCompoundClass).invoke(itemStack, nbt);
            item.setItemMeta(((ItemStack) craftItemStack.getMethod("asBukkitCopy", itemStackClass).invoke(null, itemStack)).getItemMeta());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            e.printStackTrace();
        }
        return item;
    }


    //GET NBT
    public static String getNbt(ItemStack item, String key) {
        try {
            Object itemStack = craftItemStack.getMethod("asNMSCopy", ItemStack.class).invoke(null, item);
            Object nbt = ((boolean) itemStackClass.getMethod("hasTag").invoke(itemStack)) ? itemStackClass.getMethod("getTag").invoke(itemStack) : nbtTagCompoundClass.getConstructor().newInstance();

            return (String) nbtTagCompoundClass.getMethod("getString", String.class).invoke(nbt, key);
        } catch (IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException ignored) {}
        return null;
    }


    //GET ALL COMMANDS
    public static CommandMap getCommandMap() {
        try {
            Server server = Bukkit.getServer();
            Field field = server.getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            return (CommandMap) field.get(Bukkit.getServer());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static PluginCommand registerPluginCommand(Plugin plugin, String name) {
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);
            PluginCommand pluginCommand = constructor.newInstance(name, plugin);

            getCommandMap().register(plugin.getName(), pluginCommand);
            return pluginCommand;
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            return null;
        }
    }

    public static void unRegisterPluginCommand(PluginCommand pluginCommand) {
        try {
            CommandMap commandMap = getCommandMap();
            Field field = commandMap.getClass().getDeclaredField("knownCommands");
            field.setAccessible(true);
            ((HashMap<?,?>) field.get(commandMap)).remove(pluginCommand.getLabel(), pluginCommand);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
